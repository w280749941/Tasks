package com.heartiger.taskapigateway.enums;

public enum ResultEnum {

  ACTION_SUCCESS(0, "Success"),
  UNAUTHORIZED_ACCESS(-1, "Login required"),
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
