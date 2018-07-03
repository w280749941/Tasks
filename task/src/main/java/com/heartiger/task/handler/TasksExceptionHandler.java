package com.heartiger.task.handler;

import static com.heartiger.task.constant.Constants.TASK_HEADER;

import com.heartiger.task.dto.ResultDTO;
import com.heartiger.task.exception.TasksException;
import com.heartiger.task.utils.ResultDTOUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class TasksExceptionHandler{

    @ExceptionHandler(value = TasksException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultDTO handlerUserAccountException(TasksException ex, HttpServletRequest request) {
        Object requestUri = request.getRequestURI();
        String token = request.getHeader(TASK_HEADER);
        log.info("When requesting {} with token {} caused User Exception: {}", requestUri.toString(), token, ex);
        return ResultDTOUtil.error(ex.getCode(), ex.getMessage());
    }
}
