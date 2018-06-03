package com.heartiger.task.handler;

import com.heartiger.task.dto.ResultDTO;
import com.heartiger.task.exception.TasksException;
import com.heartiger.task.utils.ResultDTOUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TasksExceptionHandler{

    @ExceptionHandler(value = TasksException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultDTO handlerUserAccountException(TasksException ex) {
        return ResultDTOUtil.error(ex.getCode(), ex.getMessage());
    }
}
