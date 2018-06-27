package com.heartiger.task_user.dto.message;


import lombok.Data;


@Data
public class UserInfoDto {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

}
