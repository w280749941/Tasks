package com.heartiger.task.message;

import com.google.gson.Gson;
import com.heartiger.task.message.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserInfoReceiver {

    private final Gson gson;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String USER_INFO_TEMPLATE = "user_info_%s";

    @Autowired
    public UserInfoReceiver(Gson gson, StringRedisTemplate stringRedisTemplate) {
        this.gson = gson;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //TODO Don't user UserInfo here, create a common pacakge to store DTO
    @RabbitListener(queuesToDeclare = @Queue("userInfo"))
    public void process(String message){
        UserInfo user = gson.fromJson(message, UserInfo.class);

        log.info("Received message from RabbitMQ {}: {}", "UserInfo", user);

        stringRedisTemplate.opsForValue().set(
                String.format(USER_INFO_TEMPLATE, user.getUserId()),
                user.getEmail());

    }
}
