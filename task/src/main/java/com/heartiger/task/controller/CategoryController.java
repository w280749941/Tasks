package com.heartiger.task.controller;

import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.dto.ResultDTO;
import com.heartiger.task.enums.ResultEnum;
import com.heartiger.task.form.CategoryForm;
import com.heartiger.task.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("user/{id}")
    public ResultDTO<List<CategoryInfo>> getAllCategories(@PathVariable int id){
        ResultDTO<List<CategoryInfo>> resultDTO = new ResultDTO<>(ResultEnum.ACTION_SUCCESS);
        resultDTO.setData(categoryService.findCategoriesByOwnerId(id));
        return resultDTO;
    }

    @GetMapping("/search")
    public ResultDTO<CategoryInfo> findCategoryByIdAndUserId(@RequestParam int cid, @RequestParam int oid) {
        Optional<CategoryInfo> result = categoryService.findCategoryByIdAndUserId(cid, oid);
        if(result.isPresent()) {
            ResultDTO<CategoryInfo> resultDTO = new ResultDTO<>(ResultEnum.ACTION_SUCCESS);
            resultDTO.setData(result.get());
            return resultDTO;
        }
        return new ResultDTO<>(ResultEnum.PARAMS_ERROR);
    }

    @PostMapping("/new")
    public ResultDTO<CategoryInfo> createCategory(@Valid CategoryForm categoryForm, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            throw new Exception("Form result not good");
            //TODO use enum, create exception, exception handler.
        }
        ResultDTO<CategoryInfo> resultDTO = new ResultDTO<>(ResultEnum.ACTION_SUCCESS);
        CategoryInfo categoryInfo = new CategoryInfo();
        //TODO need to use better property checking and exiting fields.
        categoryInfo.setTitle(categoryForm.getTitle());
        categoryInfo.setPriority(categoryForm.getPriority());
        categoryInfo.setOwnerId(categoryForm.getOwnerId());
        categoryInfo.setIsDeleted(false);
        resultDTO.setData(categoryService.saveCategoryInfo(categoryInfo));
        return resultDTO;
    }

    @PostMapping("/edit/{id}")
    public ResultDTO<CategoryInfo> createCategory(@PathVariable int id, @Valid CategoryForm categoryForm, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            throw new Exception("Form result not good");
            //TODO use enum, create exception, exception handler.
        }

        Optional<CategoryInfo> categoryToUpdate = categoryService.findCategoryByIdAndUserId(id, categoryForm.getOwnerId());

        if(!categoryToUpdate.isPresent())return new ResultDTO<>(ResultEnum.PARAMS_ERROR);

        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtils.copyProperties(categoryToUpdate.get(), categoryInfo);
        //TODO need to use better property checking and exiting fields.
        categoryInfo.setTitle(categoryForm.getTitle());
        categoryInfo.setPriority(categoryForm.getPriority());
        categoryInfo.setOwnerId(categoryForm.getOwnerId());
        categoryInfo.setIsDeleted(categoryForm.getIsDeleted());
        categoryInfo.setUpdatedTime(new Date());

        ResultDTO<CategoryInfo> resultDTO = new ResultDTO<>(ResultEnum.ACTION_SUCCESS);
        resultDTO.setData(categoryService.saveCategoryInfo(categoryInfo));
        return resultDTO;
    }
}
