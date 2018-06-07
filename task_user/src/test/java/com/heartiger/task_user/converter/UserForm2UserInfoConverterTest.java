package com.heartiger.task_user.converter;

import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.form.UserForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UserForm2UserInfoConverterTest {

    private UserForm userFormSource;

    private UserInfo userInfoToUpdate;
    private UserInfo userInfoExpected;

    @Before
    public void setup() {
        userFormSource = new UserForm();
        userFormSource.setEmail("myemail@somewhere.com");
        userFormSource.setFirstName("My first name");
        userFormSource.setLastName("My last name");
        userFormSource.setPasscode("SomeWhere");
        userFormSource.setIsActive(true);

        userInfoToUpdate = new UserInfo();
        userInfoToUpdate.setEmail("prevous@old.com");
        userInfoToUpdate.setFirstName("previous");
        userInfoToUpdate.setLastName("Old");
        userInfoToUpdate.setPasscode("badPassword");
        userInfoToUpdate.setIsActive(false);

        userInfoExpected = new UserInfo();
        userInfoExpected.setEmail("myemail@somewhere.com");
        userInfoExpected.setFirstName("My first name");
        userInfoExpected.setLastName("My last name");
        userInfoExpected.setPasscode("SomeWhere");
        userInfoExpected.setIsActive(true);
    }

    @Test
    public void convertUserFormToANewUserInfoShouldMatchExpected() {
        UserInfo userInfoDest = UserForm2UserInfoConverter.convert(userFormSource);
        Assert.assertEquals(userInfoExpected, userInfoDest);
    }

    @Test
    public void convertUserFormToAnExistingUserInfoWithOldDataShouldMatchExpected() {
        UserInfo userInfoDest = UserForm2UserInfoConverter.convertWithOldData(userFormSource, userInfoToUpdate);

        // Since updated time is set in the function, update the expected as well.
        userInfoExpected.setUpdatedTime(userInfoDest.getUpdatedTime());
        Assert.assertEquals(userInfoExpected, userInfoDest);
    }
}