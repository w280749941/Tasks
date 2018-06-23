package com.heartiger.task_user.service.impl;

import com.google.gson.Gson;
import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.repository.UserInfoRepository;
import com.heartiger.task_user.service.UserService;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AmqpTemplate amqpTemplate;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserInfoRepository userInfoRepository;

    private final Gson gson;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository, BCryptPasswordEncoder passwordEncoder, AmqpTemplate amqpTemplate, Gson gson) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.amqpTemplate = amqpTemplate;
        this.gson = gson;
    }


    @Override
    public Optional<UserInfo> findUserById(Integer userId) {
        return userInfoRepository.findById(userId);
    }

    @Override
    @Transactional
    public UserInfo saveUser(UserInfo userInfo) {
        userInfo.setPasscode(passwordEncoder.encode(userInfo.getPasscode()));
        return userInfoRepository.save(userInfo);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        //TODO delete all category, require rabbitmq controller to call another service to delete.
        Optional<UserInfo> result = findUserById(userId);
        amqpTemplate.convertAndSend("userInfo", gson.toJson(result.isPresent() ? result.get() : userId));
        //TODO should wait for the deleted confirmed message before proceeding clear user.
        userInfoRepository.deleteById(userId);
    }

    @Override
    public Optional<UserInfo> findUserByEmail(String userEmail) {
        return userInfoRepository.findUserByEmail(userEmail);
    }
}
