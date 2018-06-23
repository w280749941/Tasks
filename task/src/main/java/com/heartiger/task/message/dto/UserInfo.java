package com.heartiger.task.message.dto;


import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {


    private Integer userId;

    private String email;

    private String passcode;

    private String firstName;

    private String lastName;

    private Boolean isDeleted = false;

    private Boolean isActive = true;

    private Date createdTime;

    private Date updatedTime;
}
