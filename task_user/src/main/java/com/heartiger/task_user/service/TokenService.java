package com.heartiger.task_user.service;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.JwtToken;

public interface TokenService {

    JwtToken buildToken(UserInfo userInfo);

    String getUserName(String token);
}
