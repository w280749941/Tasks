package com.heartiger.task_user.enums;


public enum ResultEnum {

    ACTION_SUCCESS(0, "Success"),
    PARAMS_ERROR(1, "Parameters are not correct"),
    USER_PERMISSION_ERROR(2, "You can only operate on your own profile"),
    SECURITY_DEFENSE_ERROR(100, "You should not bypass the security"),
    ENTRY_NOT_FOUND(4, "The data you are looking for is not found"),
    USER_ENTRY_NOT_FOUND(5, "The user you are looking for is not found"),
    USER_ENTRY_NOT_MATCH_Authentication(6, "Db Entry and User Mismatch"),
    USER_CREDENTIAL_MISMATCH(7, "Invalid credentials"),
    USER_DELETE_COMPLETE_FAILED(8, "User entry was not deleted clearly"),
    USER_TOKEN_EXPIRED(9, "Token expired, login required"),
    USER_ENTRY_Exist(10, "Please login"),
    USER_TOKEN_INVALID(11, "Please login"),
    SERVER_ERROR(500, "Error Occured, report required"),
    API_NOT_FOUND(404, "Oops the api doesn't exist"),
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
