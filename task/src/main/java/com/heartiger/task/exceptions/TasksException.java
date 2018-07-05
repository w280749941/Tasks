package com.heartiger.task.exceptions;

import com.heartiger.task.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TasksException extends RuntimeException{

    private Integer code;
    private String message;

    public TasksException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

}
