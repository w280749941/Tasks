package com.heartiger.task.controller;

import static com.heartiger.task.controller.TaskController.notOwner;

import com.heartiger.task.converter.CategoryForm2CategoryInfoConverter;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.dtos.ResultDTO;
import com.heartiger.task.enums.ResultEnum;
import com.heartiger.task.exceptions.TasksException;
import com.heartiger.task.form.CategoryForm;
import com.heartiger.task.service.CategoryService;
import com.heartiger.task.utils.ResultDTOUtil;
import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/user/{id}")
    public ResultDTO getAllCategories(@PathVariable int id, HttpServletRequest request){
        if(notOwner(id, request))
            return ResultDTOUtil.error(ResultEnum.CATEGORY_PERMISSION_ERROR);

        return ResultDTOUtil.success(categoryService.findCategoriesByOwnerId(id));
    }

    @GetMapping("/search")
    public ResultDTO findCategoryByIdAndUserId(@RequestParam int cid, @RequestParam int oid, HttpServletRequest request) {

        if(notOwner(oid, request))
            return ResultDTOUtil.error(ResultEnum.CATEGORY_PERMISSION_ERROR);

        Optional<CategoryInfo> result = categoryService.findCategoryByIdAndUserId(cid, oid);
        if(result.isPresent())
            return ResultDTOUtil.success(result.get());

        return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);
    }

    @PostMapping("/new")
    public ResultDTO createCategory(@Valid CategoryForm categoryForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors())
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        if(notOwner(categoryForm.getOwnerId(), request))
            return ResultDTOUtil.error(ResultEnum.CATEGORY_PERMISSION_ERROR);

        CategoryInfo categoryInfo = CategoryForm2CategoryInfoConverter.convert(categoryForm);
        return ResultDTOUtil.success(categoryService.saveCategoryInfo(categoryInfo));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO editCategory(@PathVariable int id, @Valid CategoryForm categoryForm,
        BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        if(notOwner(categoryForm.getOwnerId(), request))
            return ResultDTOUtil.error(ResultEnum.CATEGORY_PERMISSION_ERROR);

        Optional<CategoryInfo> categoryToUpdate = categoryService.findCategoryByIdAndUserId(id, categoryForm.getOwnerId());
        if(!categoryToUpdate.isPresent()) return new ResultDTO<>(ResultEnum.PARAMS_ERROR);

        CategoryInfo categoryInfo = CategoryForm2CategoryInfoConverter.convertWithOldData(categoryForm, categoryToUpdate.get());

        return ResultDTOUtil.success(categoryService.saveCategoryInfo(categoryInfo));
    }

    @DeleteMapping("/delete")
    public ResultDTO deleteCategory(@RequestParam int cid, @RequestParam int oid, HttpServletRequest request){

        if(notOwner(oid, request))
            return ResultDTOUtil.error(ResultEnum.CATEGORY_PERMISSION_ERROR);

        Optional<CategoryInfo> result = categoryService.findCategoryByIdAndUserId(cid, oid);
        if(!result.isPresent())
            return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);

        categoryService.deleteCategoryByIdAndUserId(cid,oid);
        return ResultDTOUtil.success();
    }
}
