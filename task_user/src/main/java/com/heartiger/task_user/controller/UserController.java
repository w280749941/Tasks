package com.heartiger.task_user.controller;

import com.heartiger.task_user.converter.UserForm2UserInfoConverter;
import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.enums.ResultEnum;
import com.heartiger.task_user.exception.UserException;
import com.heartiger.task_user.form.UserForm;
import com.heartiger.task_user.service.UserService;
import com.heartiger.task_user.utils.ResultDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/search/{id}")
    public ResultDTO<Object> findUserById(@PathVariable int id) {
        Optional<UserInfo> result = userService.findUserById(id);
        return result.isPresent()
                ? ResultDTOUtil.success(result.get())
                : ResultDTOUtil.error(ResultEnum.USER_ENTRY_NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteUserById(@PathVariable int id) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (userEmail == null) throw new UserException(ResultEnum.SECURITY_DEFENSE_ERROR);

        Optional<UserInfo> userFound = userService.findUserByEmail(userEmail);
        Optional<UserInfo> result = userService.findUserById(id);
        if(!userFound.isPresent() || !result.isPresent() || userFound.get().getUserId() != id)
            throw new UserException(ResultEnum.USER_ENTRY_NOT_MATCH_Authentication);

        UserInfo userToDelete = result.get();
        userToDelete.setIsDeleted(true);
        userService.saveUser(userToDelete);
        return ResultDTOUtil.success();
    }

    @PostMapping("/new")
    public ResultDTO createUser(@Valid UserForm userForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            throw new UserException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        UserInfo userInfo = UserForm2UserInfoConverter.convert(userForm);
        return ResultDTOUtil.success(userService.saveUser(userInfo));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO editUser(@PathVariable int id, @Valid UserForm userForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            throw new UserException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        Optional<UserInfo> userInfoToUpdate = userService.findUserById(id);

        if(!userInfoToUpdate.isPresent()) return ResultDTOUtil.error(ResultEnum.PARAMS_ERROR);

        UserInfo userInfo = UserForm2UserInfoConverter.convertWithOldData(userForm, userInfoToUpdate.get());
        return ResultDTOUtil.success(userService.saveUser(userInfo));
    }
}
