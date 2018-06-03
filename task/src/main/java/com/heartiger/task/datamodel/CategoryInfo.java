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

    private Integer priority = 0;

    @Column(insertable=false)
    private Date createdTime;

    @Column(insertable=false)
    private Date updatedTime;

    private Boolean isDeleted = false;

    private Integer ownerId;
}
