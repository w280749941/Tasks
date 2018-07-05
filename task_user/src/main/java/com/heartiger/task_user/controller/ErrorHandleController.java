package com.heartiger.task_user.controller;

import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.enums.ResultEnum;
import com.heartiger.task_user.utils.ResultDTOUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ErrorHandleController implements ErrorController {

    @RequestMapping("/error")
    public ResultDTO handleError(HttpServletRequest request){
        Object errorException = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object errorUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorServletName = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);

        log.error("Error Exception: {}", errorException);
        log.error("Error Message: {}", errorMessage);
        log.error("Error Uri: {}", errorUri);
        log.error("Error Code: {}", errorCode);
        log.error("Error Servlet Name: {}", errorServletName);

        if(Integer.valueOf(errorCode.toString()) == HttpStatus.NOT_FOUND.value())
            return ResultDTOUtil.error(ResultEnum.API_NOT_FOUND);

        return ResultDTOUtil.error(ResultEnum.SERVER_ERROR);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
