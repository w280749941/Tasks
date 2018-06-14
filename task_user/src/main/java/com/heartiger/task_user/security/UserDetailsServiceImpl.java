package com.heartiger.task_user.security;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<UserInfo> userData = userService.findUserByEmail(s);
        if(!userData.isPresent()){
            throw new UsernameNotFoundException(s);
        }

        UserInfo user = userData.get();
        return new User(user.getEmail(), user.getPasscode(), Collections.emptyList());
    }
}
