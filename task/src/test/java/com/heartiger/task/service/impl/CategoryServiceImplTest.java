package com.heartiger.task.service.impl;

import com.heartiger.task.TaskApplicationTests;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.service.CategoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryServiceImplTest extends TaskApplicationTests {

    @Autowired
    private CategoryService categoryService;

    private Integer ownerId;
    private Integer categoryId;

    @Before
    public void setup() {
        ownerId = 1;
        categoryId = 1;
    }

    @Test
    public void findAllCategoriesShouldReturnSizeGreaterThanZero() {
        List<CategoryInfo> categoryInfoList = categoryService.findAllCategories();
        Assert.assertTrue("No category found",categoryInfoList.size() > 0);
    }

    @Test
    public void findCategoriesByOwnerIdShouldReturnSizeGreaterThanZero() {
        List<CategoryInfo> categoryInfoList = categoryService.findCategoriesByOwnerId(ownerId);
        Assert.assertTrue(String.format("No category found with owner Id %d", ownerId), categoryInfoList.size()>0);
    }

    @Test
    public void findCategoryByIdShouldReturnOne() {
        Optional<CategoryInfo> categoryInfo = categoryService.findCategoryById(categoryId);
        Assert.assertNotNull(String.format("No category found with category Id %d", categoryId), categoryInfo);
    }
}