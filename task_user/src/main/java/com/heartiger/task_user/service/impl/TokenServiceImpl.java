package com.heartiger.task_user.service.impl;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.JwtToken;
import com.heartiger.task_user.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.util.Date;
import org.springframework.stereotype.Service;

import static com.heartiger.task_user.security.SecurityConstants.EXPIRATION_TIME;
import static com.heartiger.task_user.security.SecurityConstants.SECRET;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public JwtToken buildToken(UserInfo userInfo) {

        Date expiredTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        String token = Jwts.builder()
                .setId(userInfo.getUserId().toString())
                .setSubject(userInfo.getEmail())
                .setExpiration(expiredTime)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
        return new JwtToken(userInfo.getEmail(), token, expiredTime.getTime());
    }

    @Override
    public JwtToken refreshToken(Claims claims) {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Integer.valueOf(claims.getId()));
        userInfo.setEmail(claims.getSubject());

        return buildToken(userInfo);
    }

    @Override
    public Claims getTokenClaims(String token) {
        // Retrieve and verify token

        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token).getBody();
        } catch (Exception ex){
            return null;
        }
        return claims;
    }
}