package com.example.task_user.service.impl;

import com.example.task_user.TaskUserApplicationTests;
import com.example.task_user.datamodel.UserInfo;
import com.example.task_user.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceImplTest extends TaskUserApplicationTests {

    @Autowired
    private UserService userService;

    private Integer userId;

    private UserInfo userInfo;

    @Before
    public void setup() {
        userId = 1;

        userInfo = new UserInfo();
        userInfo.setEmail("testuser@email.com");
        userInfo.setFirstName("test_first_name");
        userInfo.setLastName("test_last_name");
        userInfo.setIsActive(true);
        userInfo.setPasscode("test_password");
        userInfo.setIsDeleted(false);
    }

    @Test
    public void findUserByIdShouldReturnAnUser() {
        Optional<UserInfo> userFound = userService.findUserById(userId);
        Assert.assertTrue(String.format("User %d not found", userId),userFound.isPresent());
    }

    @Test
    @Transactional
    public void saveUserShouldReturnAnUser() {
        UserInfo userSaved = userService.saveUser(userInfo);
        Assert.assertNotNull(String.format("User %s is not saved", userInfo.getFirstName()),userSaved);
    }

    @Test
    @Transactional
    public void deleteUserShouldReturnNullWhenFindUser() {
        userService.deleteUser(userId);
        Assert.assertFalse(String.format("User %d was not deleted", userId), userService.findUserById(userId).isPresent());
    }
}