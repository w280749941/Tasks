package com.heartiger.task.converter;

import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.form.CategoryForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryForm2CategoryInfoConverterTest{

    private CategoryForm categoryFormSource;

    private CategoryInfo categoryInfoToUpdate;
    private CategoryInfo categoryExpected;

    @Before
    public void setup() {
        categoryFormSource = new CategoryForm();
        categoryFormSource.setIsDeleted(true);
        categoryFormSource.setOwnerId(10);
        categoryFormSource.setPriority(2);
        categoryFormSource.setTitle("Category form title");

        categoryInfoToUpdate = new CategoryInfo();
        categoryInfoToUpdate.setIsDeleted(false);
        categoryInfoToUpdate.setOwnerId(4);
        categoryInfoToUpdate.setPriority(5);
        categoryInfoToUpdate.setTitle("Some title");

        categoryExpected = new CategoryInfo();
        categoryExpected.setIsDeleted(true);
        categoryExpected.setOwnerId(10);
        categoryExpected.setPriority(2);
        categoryExpected.setTitle("Category form title");
    }

    @Test
    public void convertCategoryFormToANewCategoryInfoShouldMatchExpected() {
        CategoryInfo categoryInfoDest = CategoryForm2CategoryInfoConverter.convert(categoryFormSource);
        Assert.assertEquals(categoryExpected, categoryInfoDest);
    }

    @Test
    public void convertCategoryFormToAnExistingCategoryInfoWithOldDataShouldMatchExpected() {
        CategoryInfo categoryInfoDest = CategoryForm2CategoryInfoConverter.convertWithOldData(categoryFormSource, categoryInfoToUpdate);

        // Since updated time is set in the function, update the expected as well.
        categoryExpected.setUpdatedTime(categoryInfoDest.getUpdatedTime());
        Assert.assertEquals(categoryExpected, categoryInfoDest);
    }
}