package com.heartiger.task.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "categories_belong_user")
@Data
public class CategoryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cId;

    private String title;

    private Integer priority;

    private Date createdTime;

    private Date updatedTime;

    private Boolean isDeleted;

    private Integer ownerId;
}
