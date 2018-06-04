package com.heartiger.task.service.impl;

import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.repository.CategoryInfoRepository;
import com.heartiger.task.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public Optional<CategoryInfo> findCategoryByIdAndUserId(Integer categoryId, Integer ownerId) {
        return categoryInfoRepository.findByCIdAndOwnerId(categoryId, ownerId);
    }

    @Override
    public List<CategoryInfo> findCategoriesByOwnerId(Integer ownerId) {
        return categoryInfoRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public CategoryInfo saveCategoryInfo(CategoryInfo categoryInfo) {
        return categoryInfoRepository.save(categoryInfo);
    }

    @Override
    @Transactional
    public void deleteCategoryByIdAndUserId(Integer categoryId, Integer ownerId) {
        categoryInfoRepository.deleteByCIdAndOwnerId(categoryId, ownerId);
    }

    @Override
    public Optional<CategoryInfo> findById(Integer categoryId) {
        return categoryInfoRepository.findById(categoryId);
    }

}
