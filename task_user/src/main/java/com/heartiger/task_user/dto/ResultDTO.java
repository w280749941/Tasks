package com.heartiger.task_user.dto;

import com.heartiger.task_user.enums.ResultEnum;
import lombok.Data;

@Data
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public ResultDTO() {

    }

    public ResultDTO(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }
}
