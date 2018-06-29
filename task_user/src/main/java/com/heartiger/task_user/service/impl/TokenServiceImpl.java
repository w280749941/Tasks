package com.heartiger.task_user.service.impl;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.JwtToken;
import com.heartiger.task_user.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import org.springframework.stereotype.Service;

import static com.heartiger.task_user.security.SecurityConstants.EXPIRATION_TIME;
import static com.heartiger.task_user.security.SecurityConstants.SECRET;
import static com.heartiger.task_user.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public JwtToken buildToken(UserInfo userInfo) {
        Date expiredTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String token = Jwts.builder()
                .setSubject(userInfo.getEmail())
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        return new JwtToken(token, expiredTime.getTime());
    }

    @Override
    public String getUserName(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
    }
}
