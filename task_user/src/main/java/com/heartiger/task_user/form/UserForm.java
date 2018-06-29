package com.heartiger.task_user.form;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserForm extends LoginForm{


    @NotBlank(message = "Please enter a first name")
    private String firstName;

    @NotBlank(message = "Please enter a last name")
    private String lastName;

    private Boolean isDeleted;

    private Boolean isActive;
}
