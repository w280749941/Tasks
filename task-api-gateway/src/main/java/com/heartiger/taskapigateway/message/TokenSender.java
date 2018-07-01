package com.heartiger.taskapigateway.message;

import static com.heartiger.taskapigateway.constant.Constants.RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE;

import com.google.gson.Gson;
import com.heartiger.taskapigateway.message.dto.UserInfoDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenSender {

    private final AmqpTemplate amqpTemplate;
    private final Gson gson;

    @Autowired
    public TokenSender(AmqpTemplate amqpTemplate, Gson gson) {
        this.amqpTemplate = amqpTemplate;
        this.gson = gson;
    }

    public UserInfoDto getUserNameFromToken(String token){
        Object userInfoDto = amqpTemplate.convertSendAndReceive(RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE, token);
        if(StringUtils.isEmpty(userInfoDto))
            return null;
        return gson.fromJson(userInfoDto.toString(), UserInfoDto.class);
    }
}
