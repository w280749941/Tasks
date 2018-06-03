package com.heartiger.task.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class CategoryForm {

    @NotEmpty(message = "Category title is required")
    private String title;

    @NotNull(message = "Priority level is required")
    private Integer priority;

    private Boolean isDeleted;

    @NotNull(message = "User id is required")
    private Integer ownerId;
}
