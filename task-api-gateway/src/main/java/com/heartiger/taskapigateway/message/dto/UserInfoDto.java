package com.heartiger.taskapigateway.message.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

}