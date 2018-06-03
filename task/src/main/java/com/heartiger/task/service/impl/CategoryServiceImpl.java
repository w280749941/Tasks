package com.heartiger.task.service.impl;

import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.repository.CategoryInfoRepository;
import com.heartiger.task.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryInfoRepository categoryInfoRepository;

    @Autowired
    public CategoryServiceImpl(CategoryInfoRepository categoryInfoRepository) {
        this.categoryInfoRepository = categoryInfoRepository;
    }

    @Override
    public List<CategoryInfo> findAllCategories() {
        return categoryInfoRepository.findAll();
    }

    @Override
    public Optional<CategoryInfo> findCategoryById(Integer categoryId) {
        return categoryInfoRepository.findById(categoryId);
    }

    @Override
    public List<CategoryInfo> findCategoriesByOwnerId(Integer ownerId) {
        return categoryInfoRepository.findByOwnerId(ownerId);
    }
}
