package com.heartiger.task_user.converter;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.form.UserForm;

import java.util.Date;

public class UserForm2UserInfoConverter {

    public static UserInfo convert(UserForm userForm) {
        UserInfo userInfo = new UserInfo();
        convertWithUserInfo(userForm, userInfo);

        return userInfo;
    }

    public static UserInfo convertWithOldData(UserForm userForm, UserInfo userToUpdate) {
        convertWithUserInfo(userForm, userToUpdate);
        userToUpdate.setUpdatedTime(new Date());
        return userToUpdate;
    }

    private static void convertWithUserInfo(UserForm userForm, UserInfo userInfo) {
        userInfo.setEmail(userForm.getEmail());
        userInfo.setPasscode(userForm.getPasscode());
        userInfo.setFirstName(userForm.getFirstName());
        userInfo.setLastName(userForm.getLastName());
        if(userForm.getIsDeleted() != null) userInfo.setIsDeleted(userForm.getIsDeleted());
        if(userForm.getIsActive() != null) userInfo.setIsActive(userForm.getIsActive());
    }

}
