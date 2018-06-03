package com.heartiger.task.controller;

import com.heartiger.task.converter.CategoryForm2CategoryInfoConverter;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.dto.ResultDTO;
import com.heartiger.task.enums.ResultEnum;
import com.heartiger.task.exception.TasksException;
import com.heartiger.task.form.CategoryForm;
import com.heartiger.task.service.CategoryService;
import com.heartiger.task.utils.ResultDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResultDTO<Object> getAllCategories(@PathVariable int id){
        return ResultDTOUtil.success(categoryService.findCategoriesByOwnerId(id));
    }

    @GetMapping("/search")
    public ResultDTO findCategoryByIdAndUserId(@RequestParam int cid, @RequestParam int oid) {
        Optional<CategoryInfo> result = categoryService.findCategoryByIdAndUserId(cid, oid);
        if(result.isPresent())
            return ResultDTOUtil.success(result.get());

        return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);
    }

    @PostMapping("/new")
    public ResultDTO<Object> createCategory(@Valid CategoryForm categoryForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        CategoryInfo categoryInfo = CategoryForm2CategoryInfoConverter.convert(categoryForm);
        return ResultDTOUtil.success(categoryService.saveCategoryInfo(categoryInfo));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO<Object> editCategory(@PathVariable int id, @Valid CategoryForm categoryForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Optional<CategoryInfo> categoryToUpdate = categoryService.findCategoryByIdAndUserId(id, categoryForm.getOwnerId());

        if(!categoryToUpdate.isPresent()) return new ResultDTO<>(ResultEnum.PARAMS_ERROR);

        CategoryInfo categoryInfo = CategoryForm2CategoryInfoConverter.convertWithOldData(categoryForm, categoryToUpdate.get());
        return ResultDTOUtil.success(categoryService.saveCategoryInfo(categoryInfo));
    }
}
