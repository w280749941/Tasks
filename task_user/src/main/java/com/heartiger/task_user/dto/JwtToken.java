package com.heartiger.task_user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
    private String token;

    private Long expire;
}
