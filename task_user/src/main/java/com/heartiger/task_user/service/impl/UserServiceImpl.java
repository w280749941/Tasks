package com.heartiger.task_user.service.impl;

import static com.heartiger.task_user.security.SecurityConstants.RABBITMQ_DELETE_USER_COMPLETE_QUEUE;

import com.google.gson.Gson;
import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.message.UserInfoDto;
import com.heartiger.task_user.form.LoginForm;
import com.heartiger.task_user.repository.UserInfoRepository;
import com.heartiger.task_user.service.UserService;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final AmqpTemplate amqpTemplate;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserInfoRepository userInfoRepository;

    private final Gson gson;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository,
        BCryptPasswordEncoder passwordEncoder, AmqpTemplate amqpTemplate, Gson gson,
        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.amqpTemplate = amqpTemplate;
        this.gson = gson;
      this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        userInfoRepository.deleteById(userId);
    }

    @Override
    public Optional<UserInfo> findUserByEmail(String userEmail) {
        return userInfoRepository.findUserByEmail(userEmail);
    }

    @Override
    public boolean deleteUserComplete(Integer userId) {
        //delete all categories, require rabbitmq to call another service to delete.
        Optional<UserInfo> userFound = findUserById(userId);
        UserInfoDto userToSend = new UserInfoDto();
        if(!userFound.isPresent()) return false;
        BeanUtils.copyProperties(userFound.get(), userToSend);

        Object result = amqpTemplate.convertSendAndReceive(RABBITMQ_DELETE_USER_COMPLETE_QUEUE, gson.toJson(userToSend));
        Boolean categoriesDeleted = Boolean.valueOf(result.toString());
        if(!categoriesDeleted) return false;
        //wait for the deleted confirmed message before proceeding clear user.
        deleteUser(userId);
        return true;
    }

    @Override
    public UserInfo authenticateUser(LoginForm loginForm) {
        Optional<UserInfo> userFound = findUserByEmail(loginForm.getEmail());

        if(userFound.isPresent()){
          UserInfo userInfo = userFound.get();
          if(bCryptPasswordEncoder.matches(loginForm.getPasscode(), userInfo.getPasscode()))
            return userInfo;
        }

        return null;
    }
}
