package com.heartiger.task_user.service;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.form.LoginForm;

import java.util.Optional;

public interface UserService {

    UserInfo saveUser(UserInfo userInfo);

    Optional<UserInfo> findUserById(Integer userId);

    void deleteUser(Integer userId);

    Optional<UserInfo> findUserByEmail(String userEmail);

    boolean deleteUserComplete(Integer userId);

    UserInfo authenticateUser(LoginForm loginForm);
}
