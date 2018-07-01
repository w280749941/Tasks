package com.heartiger.task_user.controller;

import static com.heartiger.task_user.security.SecurityConstants.RABBITMQ_DELETE_USER_QUEUE;
import static com.heartiger.task_user.security.SecurityConstants.RABBITMQ_USER_LOGIN_QUEUE;
import static com.heartiger.task_user.security.SecurityConstants.RABBITMQ_USER_TOKEN_REFRESH_QUEUE;
import static com.heartiger.task_user.security.SecurityConstants.TASK_HEADER;
import static com.heartiger.task_user.security.SecurityConstants.TOKEN_PREFIX;

import com.google.gson.Gson;
import com.heartiger.task_user.converter.UserForm2UserInfoConverter;
import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.JwtToken;
import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.enums.ResultEnum;
import com.heartiger.task_user.exception.UserException;
import com.heartiger.task_user.form.LoginForm;
import com.heartiger.task_user.form.UserForm;
import com.heartiger.task_user.service.TokenService;
import com.heartiger.task_user.service.UserService;
import com.heartiger.task_user.utils.ResultDTOUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AmqpTemplate amqpTemplate;
    private final Gson gson;

  @Autowired
    public UserController(UserService userService, TokenService tokenService, AmqpTemplate amqpTemplate, Gson gson) {
      this.userService = userService;
      this.tokenService = tokenService;
      this.amqpTemplate = amqpTemplate;
      this.gson = gson;
  }


    @GetMapping("/search/{id}")
    public ResultDTO<Object> findUserById(@PathVariable int id) {

        Optional<UserInfo> result = userService.findUserById(id);
        return result.isPresent()
                ? ResultDTOUtil.success(result.get())
                : ResultDTOUtil.error(ResultEnum.USER_ENTRY_NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteUserById(@PathVariable int id, HttpServletRequest request) {

        // Reject delete action if userToDelete different from actual user
        UserInfo userToDelete = validationBeforeDeleteReturnUser(id, request);
        amqpTemplate.convertAndSend(RABBITMQ_DELETE_USER_QUEUE, userToDelete.getEmail());
        userToDelete.setIsDeleted(true);
        userService.saveUser(userToDelete);
        return ResultDTOUtil.success();
    }

    @DeleteMapping("/delete/complete/{id}")
    public ResultDTO deleteUserCompleteById(@PathVariable int id, HttpServletRequest request) {

        // Reject delete action if userToDelete different from actual user
        UserInfo userToDelete = validationBeforeDeleteReturnUser(id, request);
        amqpTemplate.convertAndSend(RABBITMQ_DELETE_USER_QUEUE, userToDelete.getEmail());
        Boolean deleteResult = userService.deleteUserComplete(id);
        if(deleteResult)
            return ResultDTOUtil.success();
        return ResultDTOUtil.error(ResultEnum.USER_DELETE_COMPLETE_FAILED);
    }

    @PostMapping("/new")
    public ResultDTO createUser(@Valid UserForm userForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            throw new UserException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        Optional<UserInfo> userFound = userService.findUserByEmail(userForm.getEmail());
        if(userFound.isPresent())
            return ResultDTOUtil.error(ResultEnum.USER_ENTRY_Exist);

        UserInfo userInfo = UserForm2UserInfoConverter.convert(userForm);
        return ResultDTOUtil.success(userService.saveUser(userInfo));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO editUser(@PathVariable int id, @Valid UserForm userForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors())
            throw new UserException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        UserInfo userInfoToUpdate = validationBeforeDeleteReturnUser(id, request);
        UserInfo userInfo = UserForm2UserInfoConverter.convertWithOldData(userForm, userInfoToUpdate);
        return ResultDTOUtil.success(userService.saveUser(userInfo));
    }

    private UserInfo validationBeforeDeleteReturnUser(int id, HttpServletRequest request) {
        String token = getClaimsFromHeader(request);
        Claims claims = tokenService.getTokenClaims(token);

        if (claims == null)
            throw new UserException(ResultEnum.USER_TOKEN_INVALID);

        if(!claims.getId().equals(String.valueOf(id)))
            throw new UserException(ResultEnum.USER_PERMISSION_ERROR);

        Optional<UserInfo> result = userService.findUserById(id);
        if(!result.isPresent())
            throw new UserException(ResultEnum.PARAMS_ERROR);

        return result.get();
    }

    @PostMapping("/login")
    public ResultDTO createUser(@Valid LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {

        if(bindingResult.hasErrors())
          throw new UserException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        UserInfo userInfo = userService.authenticateUser(loginForm);
        if(userInfo == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return ResultDTOUtil.error(ResultEnum.USER_CREDENTIAL_MISMATCH);
        }
        JwtToken jwtToken = tokenService.buildToken(userInfo);

        response.setHeader(TASK_HEADER, TOKEN_PREFIX + jwtToken.getToken());
        Object result = amqpTemplate.convertSendAndReceive(RABBITMQ_USER_LOGIN_QUEUE, gson.toJson(jwtToken));
        Boolean tokenSaved = Boolean.valueOf(result.toString());
        if(tokenSaved)
            log.info("Token created and saved for user: {}", userInfo);
        else
            log.info("Token created but not saved for user: {}", userInfo);

        return ResultDTOUtil.success(jwtToken);
    }

    @PostMapping("/token/refresh")
    public ResultDTO refreshToken(HttpServletRequest request, HttpServletResponse response){

        String token = getClaimsFromHeader(request);
        Claims claims = tokenService.getTokenClaims(token);

        if (claims == null)
            throw new UserException(ResultEnum.USER_TOKEN_INVALID);

        if(!claims.getExpiration().after(new Date(System.currentTimeMillis())))
            return ResultDTOUtil.error(ResultEnum.USER_TOKEN_EXPIRED);

        JwtToken jwtToken = tokenService.refreshToken(claims);
        response.setHeader(TASK_HEADER, TOKEN_PREFIX + jwtToken.getToken());

        Object result = amqpTemplate.convertSendAndReceive(RABBITMQ_USER_TOKEN_REFRESH_QUEUE, gson.toJson(jwtToken));
        Boolean tokenSaved = Boolean.valueOf(result.toString());
        if(tokenSaved)
            log.info("Token refreshed and saved for user: {}", claims);
        else
            log.info("Token refreshed but not saved for user: {}", claims);

        return ResultDTOUtil.success(jwtToken);
    }

    private String getClaimsFromHeader(HttpServletRequest request){
        String tokenFromRequest = request.getHeader(TASK_HEADER);
        return tokenFromRequest.replace(TOKEN_PREFIX, "");
    }
}
