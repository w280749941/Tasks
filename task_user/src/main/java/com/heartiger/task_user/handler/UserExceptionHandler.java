package com.heartiger.task_user.handler;

import static com.heartiger.task_user.security.SecurityConstants.TASK_HEADER;

import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.exception.UserException;
import com.heartiger.task_user.utils.ResultDTOUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultDTO handlerUserAccountException(UserException ex, HttpServletRequest request) {
        Object requestUri = request.getRequestURI();
        String token = request.getHeader(TASK_HEADER);
        log.info("When requesting {} with token {} caused User Exception: {}", requestUri.toString(), token, ex);
        return ResultDTOUtil.error(ex.getCode(), ex.getMessage());
    }
}
