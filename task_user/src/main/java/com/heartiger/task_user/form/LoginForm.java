package com.heartiger.task_user.form;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginForm {

    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please enter a passcode")
    private String passcode;
}
