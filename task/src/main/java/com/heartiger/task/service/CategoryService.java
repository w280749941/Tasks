package com.heartiger.task.service;

import com.heartiger.task.datamodel.CategoryInfo;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<CategoryInfo> findCategoryByIdAndUserId(Integer categoryId, Integer ownerId);

    List<CategoryInfo> findCategoriesByOwnerId(Integer ownerId);

    CategoryInfo saveCategoryInfo(CategoryInfo categoryInfo);

    void deleteCategoryByIdAndUserId(Integer categoryId, Integer ownerId);

    Optional<CategoryInfo> findById(Integer categoryId);

    void deleteCategoryByUserId(Integer ownerId);
}
