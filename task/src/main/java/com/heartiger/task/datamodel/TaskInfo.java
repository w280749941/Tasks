package com.heartiger.task.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks_from_user")
@Data
public class TaskInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tId;

    private String title;

    private String description;

    private Integer priority;

    private Date createdTime;

    private Date updatedTime;

    private Date due_time;

    private Date reminder_time;

    private Boolean isCompleted;

    private Boolean isDeleted;

    private Integer ownerId;

    private Integer categoryId;
}
