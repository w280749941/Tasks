package com.heartiger.task.service;

import com.heartiger.task.datamodel.CategoryInfo;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryInfo> findAllCategories();

    Optional<CategoryInfo> findCategoryById(Integer categoryId);

    List<CategoryInfo> findCategoriesByOwnerId(Integer ownerId);
}
