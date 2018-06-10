package com.heartiger.task_user.service.impl;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.repository.UserInfoRepository;
import com.heartiger.task_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
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
        //TODO delete all category, require feign controller to call another service to delete.
        userInfoRepository.deleteById(userId);
    }
}
