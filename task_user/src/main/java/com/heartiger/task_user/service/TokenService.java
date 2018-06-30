package com.heartiger.task_user.service;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.JwtToken;
import io.jsonwebtoken.Claims;

public interface TokenService {

    JwtToken buildToken(UserInfo userInfo);

    JwtToken refreshToken(Claims claims);

    Claims getTokenClaims(String token);
}
