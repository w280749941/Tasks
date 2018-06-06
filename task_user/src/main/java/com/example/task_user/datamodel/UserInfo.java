package com.example.task_user.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String email;

    private String passcode;

    private String firstName;

    private String lastName;

    private Boolean isDeleted;

    private Boolean isActive;

    @Column(insertable=false)
    private Date createdTime;

    @Column(insertable=false)
    private Date updatedTime;
}
