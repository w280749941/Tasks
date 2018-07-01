package com.heartiger.task_user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {

    @JsonIgnore
    private String userName;

    private String token;

    private Long expire;
}
