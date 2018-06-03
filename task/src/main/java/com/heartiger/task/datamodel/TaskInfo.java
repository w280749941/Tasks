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

    private Integer priority = 0;

    @Column(insertable=false)
    private Date createdTime;

    @Column(insertable=false)
    private Date updatedTime;

    private Date due_time;

    private Date reminder_time;

    private Boolean isCompleted = false;

    private Boolean isDeleted = false;

    private Integer ownerId;

    private Integer categoryId;
}
