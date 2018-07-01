package com.heartiger.task_user.message;


import static com.heartiger.task_user.security.SecurityConstants.RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE;

import com.google.gson.Gson;
import com.heartiger.task_user.dto.JwtToken;
import com.heartiger.task_user.enums.ResultEnum;
import com.heartiger.task_user.exception.UserException;
import com.heartiger.task_user.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenReceiver {

    private final TokenService tokenService;

    @Autowired
    public TokenReceiver(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @RabbitListener(queuesToDeclare = @Queue(RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE))
    public String tokenProcess(String token){

        log.info("Received user token from RabbitMQ to get username{}: {}", RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE, token);
        Claims claims = tokenService.getTokenClaims(token);
        if (claims == null)
            return "";

        return claims.getSubject();
    }
}
