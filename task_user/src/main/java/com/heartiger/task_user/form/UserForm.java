package com.heartiger.task_user.form;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


@Data
public class UserForm {

    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please enter a passcode")
    private String passcode;

    @NotBlank(message = "Please enter a first name")
    private String firstName;

    @NotBlank(message = "Please enter a last name")
    private String lastName;

    private Boolean isDeleted;

    private Boolean isActive;
}
