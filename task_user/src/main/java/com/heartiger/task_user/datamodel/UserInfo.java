package com.heartiger.task_user.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String passcode;

    private String firstName;

    private String lastName;

    private Boolean isDeleted = false;

    private Boolean isActive = true;

    @Column(insertable=false)
    private Date createdTime;

    @Column(insertable=false)
    private Date updatedTime;
}
