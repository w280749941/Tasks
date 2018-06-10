package com.heartiger.task_user.requestmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryUserModel {
    String cid; // CategoryID
    String oid; // OwnerID
}
