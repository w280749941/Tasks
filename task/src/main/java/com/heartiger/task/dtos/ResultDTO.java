package com.heartiger.task.dtos;

import com.heartiger.task.enums.ResultEnum;
import lombok.Data;

@Data
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public ResultDTO () {

    }

    public ResultDTO (ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }
}
