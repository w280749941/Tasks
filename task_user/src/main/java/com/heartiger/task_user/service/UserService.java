package com.heartiger.task_user.service;

import com.heartiger.task_user.datamodel.UserInfo;

import java.util.Optional;

public interface UserService {

    UserInfo saveUser(UserInfo userInfo);

    Optional<UserInfo> findUserById(Integer userId);

    void deleteUser(Integer userId);

    Optional<UserInfo> findUserByEmail(String userEmail);
}
