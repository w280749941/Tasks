package com.heartiger.task.message;

import com.google.gson.Gson;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.message.dto.UserInfoDto;
import com.heartiger.task.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserInfoReceiver {

    private final Gson gson;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String USER_INFO_TEMPLATE = "user_info_%s";
    private final CategoryService categoryService;

    @Autowired
    public UserInfoReceiver(Gson gson, StringRedisTemplate stringRedisTemplate, CategoryService categoryService) {
        this.gson = gson;
        this.stringRedisTemplate = stringRedisTemplate;
        this.categoryService = categoryService;
    }

    @RabbitListener(queuesToDeclare = @Queue("userToDeleteComplete"))
    public Boolean process(String message){

        UserInfoDto user = gson.fromJson(message, UserInfoDto.class);
        if (user == null) return false;

        categoryService.deleteCategoryByUserId(user.getUserId());
        log.info("Received message from RabbitMQ {}: {}", "userToDeleteComplete", user);
        //Redis can be used for other stuff.
        stringRedisTemplate.opsForValue().set(
                String.format(USER_INFO_TEMPLATE, user.getUserId()),
                user.getEmail());
        List<CategoryInfo> lt = categoryService.findCategoriesByOwnerId(user.getUserId());
        return lt.isEmpty();
    }
}
