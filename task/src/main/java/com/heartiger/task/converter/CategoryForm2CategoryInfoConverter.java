package com.heartiger.task.converter;

import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.form.CategoryForm;
import org.springframework.beans.BeanUtils;

import java.util.Date;


public class CategoryForm2CategoryInfoConverter {

    /**
     *  Add categoryForm data to a new CategoryInfo
     */
    public static CategoryInfo convert(CategoryForm categoryForm) {

        CategoryInfo categoryInfo = new CategoryInfo();
        convertWithCategoryInfo(categoryForm, categoryInfo);

        return categoryInfo;
    }

    /**
     * Add categoryForm data to CategoryInfo from database.
     */
    public static CategoryInfo convertWithOldData(CategoryForm categoryForm, CategoryInfo categoryToUpdate) {

        convertWithCategoryInfo(categoryForm, categoryToUpdate);
        categoryToUpdate.setUpdatedTime(new Date());

        return categoryToUpdate;
    }

    private static void convertWithCategoryInfo(CategoryForm categoryForm, CategoryInfo categoryInfo) {

        categoryInfo.setTitle(categoryForm.getTitle());
        categoryInfo.setPriority(categoryForm.getPriority());
        categoryInfo.setOwnerId(categoryForm.getOwnerId());

        if(categoryForm.getIsDeleted() != null) categoryInfo.setIsDeleted(categoryForm.getIsDeleted());
    }
}
