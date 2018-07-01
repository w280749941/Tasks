package com.heartiger.taskapigateway.message;

import static com.heartiger.taskapigateway.constant.Constants.RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenSender {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public TokenSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public String getUserNameFromToken(String token){
        Object userName = amqpTemplate.convertSendAndReceive(RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE, token);
        if(!StringUtils.isEmpty(userName))
            return userName.toString();
        return "";
    }
}
