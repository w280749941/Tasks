package com.heartiger.task_user.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please enter a passcode")
    private String passcode;
}
