package com.heartiger.task.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class TaskForm {

    @NotEmpty(message = "Task title is required")
    private String title;

    @NotEmpty(message = "Task description is required")
    private String description;

    @NotNull(message = "Priority level is required")
    private Integer priority;

    private String dueTime;

    private String reminderTime;

    private Boolean isCompleted;

    private Boolean isDeleted;

    @NotNull(message = "user id is required")
    private Integer ownerId;

    @NotNull(message = "category id is required")
    private Integer categoryId;
}
