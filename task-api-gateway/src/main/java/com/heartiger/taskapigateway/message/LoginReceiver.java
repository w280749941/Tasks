package com.heartiger.taskapigateway.message;

import static com.heartiger.taskapigateway.constant.RedisConstant.USER_TOKEN_TEMPLATE;

import com.google.gson.Gson;
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
public class LoginReceiver {
  private final Gson gson;
  private final StringRedisTemplate stringRedisTemplate;

  @Autowired
  public LoginReceiver(Gson gson, StringRedisTemplate stringRedisTemplate) {
    this.gson = gson;
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @RabbitListener(queuesToDeclare = @Queue("userLogin"))
  public Boolean process(String message){

    JwtToken jwtToken = gson.fromJson(message, JwtToken.class);
    log.info("Received user token from RabbitMQ {}: {}", "userLogin", jwtToken);

    stringRedisTemplate.opsForValue().set(
        String.format(USER_TOKEN_TEMPLATE, jwtToken.getToken()), gson.toJson(jwtToken), 30, TimeUnit.MINUTES);
    log.info("Token saved to Redis");
    return true;
  }
}
