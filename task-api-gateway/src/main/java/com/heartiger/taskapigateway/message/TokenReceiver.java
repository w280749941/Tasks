package com.heartiger.taskapigateway.message;

import static com.heartiger.taskapigateway.constant.Constants.RABBITMQ_DELETE_USER_QUEUE;
import static com.heartiger.taskapigateway.constant.Constants.RABBITMQ_USER_LOGIN_QUEUE;
import static com.heartiger.taskapigateway.constant.Constants.RABBITMQ_USER_TOKEN_REFRESH_QUEUE;
import static com.heartiger.taskapigateway.constant.Constants.USER_TOKEN_TEMPLATE;

import com.google.gson.Gson;
import com.heartiger.taskapigateway.constant.Constants;
import com.heartiger.taskapigateway.message.dto.JwtToken;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenReceiver {
    private final Gson gson;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public TokenReceiver(Gson gson, StringRedisTemplate stringRedisTemplate) {
      this.gson = gson;
      this.stringRedisTemplate = stringRedisTemplate;
    }

    @RabbitListener(queuesToDeclare = @Queue(RABBITMQ_USER_LOGIN_QUEUE))
    public Boolean loginProcess(String message){

      JwtToken jwtToken = gson.fromJson(message, JwtToken.class);
      log.info("Received user token from RabbitMQ {}: {}", RABBITMQ_USER_LOGIN_QUEUE, jwtToken);

      stringRedisTemplate.opsForValue().set(
          String.format(USER_TOKEN_TEMPLATE, jwtToken.getToken()), jwtToken.getToken(), 30, TimeUnit.MINUTES);
      log.info("Token saved to Redis");
      return true;
    }

    @RabbitListener(queuesToDeclare = @Queue(RABBITMQ_USER_TOKEN_REFRESH_QUEUE))
    public Boolean tokenRefreshProcess(String message){

        String[] tokens = gson.fromJson(message, String[].class);
        log.info("Received user token from RabbitMQ {}: {}", RABBITMQ_USER_TOKEN_REFRESH_QUEUE, tokens);

        stringRedisTemplate.delete(String.format(USER_TOKEN_TEMPLATE, tokens[0]));
        stringRedisTemplate.opsForValue().set(
            String.format(USER_TOKEN_TEMPLATE, tokens[1]), tokens[1], 30, TimeUnit.MINUTES);
        log.info("Token saved to Redis");
        return true;
    }

    @RabbitListener(queuesToDeclare = @Queue(RABBITMQ_DELETE_USER_QUEUE))
    public void deleteUserProcess(String message){

        log.info("Received user token from RabbitMQ {}: {}", RABBITMQ_DELETE_USER_QUEUE, message);
        stringRedisTemplate.delete(String.format(USER_TOKEN_TEMPLATE, message));
        log.info("Token removed from Redis");
    }
}
