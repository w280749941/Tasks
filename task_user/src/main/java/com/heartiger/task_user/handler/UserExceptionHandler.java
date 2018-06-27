package com.heartiger.task_user.handler;

import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.exception.UserException;
import com.heartiger.task_user.utils.ResultDTOUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultDTO handlerUserAccountException(UserException ex) {
        return ResultDTOUtil.error(ex.getCode(), ex.getMessage());
    }
}
