package com.heartiger.task_user.controller;

import com.heartiger.task_user.client.CategoryClient;
import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.requestmodel.CategoryUserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CategoryClientController {

    private final CategoryClient categoryClient;

    @Autowired
    public CategoryClientController(CategoryClient categoryClient) {
        this.categoryClient = categoryClient;
    }

    @DeleteMapping("/getProductMsg")
    public ResultDTO deleteCategoryWithOwnerId(){
        ResultDTO response = categoryClient.deleteCategory(21,1);
        log.info("response={}", response);
        return response;
    }
}
