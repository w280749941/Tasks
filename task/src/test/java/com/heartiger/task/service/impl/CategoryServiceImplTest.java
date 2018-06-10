package com.heartiger.task.service.impl;

import com.heartiger.task.TaskApplicationTests;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.service.CategoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class CategoryServiceImplTest extends TaskApplicationTests {

    @Autowired
    private CategoryService categoryService;

    private Integer ownerId;
    private Integer categoryId;
    private CategoryInfo categoryInfo;

    @Before
    public void setup() {
        ownerId = 1;
        categoryId = 1;
        categoryInfo = new CategoryInfo();
        categoryInfo.setOwnerId(1);
        categoryInfo.setTitle("One category");
    }

    @Test
    public void findCategoriesByOwnerIdShouldReturnSizeGreaterThanZero() {
        List<CategoryInfo> categoryInfoList = categoryService.findCategoriesByOwnerId(ownerId);
        Assert.assertTrue(String.format("No category found with owner Id %d", ownerId), categoryInfoList.size()>0);
    }

    @Test
    public void findCategoryByIdShouldReturnOne() {
        Optional<CategoryInfo> categoryInfo = categoryService.findById(categoryId);
        Assert.assertNotNull(String.format("No category found with category Id %d", categoryId), categoryInfo);
    }

    @Test
    public void findCategoryByIdAndUserIdShouldReturnOne() {
        Optional<CategoryInfo> categoryInfo = categoryService.findCategoryByIdAndUserId(categoryId, ownerId);
        Assert.assertNotNull(String.format("No category found with category Id %d and owner Id %d", categoryId, ownerId), categoryInfo);
    }

    @Test
    @Transactional
    public void saveCategoryInfoShouldReturnOne() {
        CategoryInfo returnedCategoryInfo = categoryService.saveCategoryInfo(categoryInfo);
        Assert.assertNotNull("Category can't be saved", returnedCategoryInfo);
    }

    @Test
    @Transactional
    public void deleteCategoryInfoShouldDeleteAllTasksAndFoundNull() {
        categoryService.deleteCategoryByIdAndUserId(categoryId, ownerId);
        Assert.assertFalse(categoryService.findCategoryByIdAndUserId(categoryId, ownerId).isPresent());
    }

    @Test
    @Transactional
    public void deleteCategoriesShouldDeleteAllCategoriesAndTasksAndFoundNull() {
        categoryService.deleteCategoryByIdAndUserId(categoryId, ownerId);
        Assert.assertFalse(categoryService.findCategoryByIdAndUserId(categoryId, ownerId).isPresent());
    }

    @Test
    @Transactional
    public void updateCategoryInfoShouldReturnOne() {
        Optional<CategoryInfo> categoryToUpdate = categoryService.findCategoryByIdAndUserId(1,1);
        if(categoryToUpdate.isPresent()) {
            categoryToUpdate.get().setTitle("update Category");
            Assert.assertNotNull("Category can't be saved", categoryService.saveCategoryInfo(categoryToUpdate.get()));
        }
    }
}