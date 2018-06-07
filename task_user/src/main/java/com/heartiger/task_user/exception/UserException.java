package com.heartiger.task_user.exception;

import com.heartiger.task_user.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException{

    private Integer code;
    private String message;

    public UserException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

}
