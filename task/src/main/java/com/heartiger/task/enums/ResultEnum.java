package com.heartiger.task.enums;


public enum ResultEnum {

    ACTION_SUCCESS(0, "Success"),
    PARAMS_ERROR(1, "Parameters are not correct"),
    CATEGORY_PERMISSION_ERROR(2, "You can only operate on your own category"),
    ENTRY_NOT_FOUND(4, "The data you are looking for is not found"),
    CATEGORY_ENTRY_NOT_FOUND(5, "The category you are looking for is not found"),
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
