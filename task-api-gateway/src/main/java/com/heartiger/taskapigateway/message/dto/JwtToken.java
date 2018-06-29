package com.heartiger.taskapigateway.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
  private String token;

  private Long expire;
}
