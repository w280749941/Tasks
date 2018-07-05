package com.heartiger.task.controller;

import static com.heartiger.task.constants.Constants.HEADER_USER_ID;

import com.heartiger.task.converter.TaskForm2TaskInfoConverter;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.dtos.ResultDTO;
import com.heartiger.task.enums.ResultEnum;
import com.heartiger.task.exceptions.TasksException;
import com.heartiger.task.form.TaskForm;
import com.heartiger.task.service.CategoryService;
import com.heartiger.task.service.TaskService;
import com.heartiger.task.utils.ResultDTOUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;

    @Autowired
    public TaskController(TaskService taskService, CategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping("/user/{id}")
    public ResultDTO getAllTasks(@PathVariable int id, HttpServletRequest request){

        if(notOwner(id, request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);

        return ResultDTOUtil.success(taskService.findAllTasksByOwnerId(id));
    }

    @GetMapping("/search")
    public ResultDTO findTaskByIdAndUserId(@RequestParam int tid, @RequestParam int oid, HttpServletRequest request) {

        if(notOwner(oid, request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);
        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(result.isPresent()) {
            return ResultDTOUtil.success(result.get());
        }
        return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);
    }

    @PostMapping("/new")
    public ResultDTO createTask(@Valid TaskForm taskForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors())
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        if(notOwner(taskForm.getOwnerId(), request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);

        Optional<CategoryInfo> categoryInfo = categoryService
            .findCategoryByIdAndUserId(taskForm.getCategoryId(), taskForm.getOwnerId());
        if(!categoryInfo.isPresent())
            return ResultDTOUtil.error(ResultEnum.CATEGORY_ENTRY_NOT_FOUND);

        TaskInfo taskInfo = TaskForm2TaskInfoConverter.convert(taskForm);
        return ResultDTOUtil.success(taskService.saveTaskInfo(taskInfo));
    }

    @PostMapping("/complete")
    public ResultDTO completeTask(@RequestParam int tid, @RequestParam int oid, HttpServletRequest request) {

        if(notOwner(oid, request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);

        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(!result.isPresent())
            ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);

        TaskInfo taskToComplete = result.get();
        taskToComplete.setIsCompleted(true);
        return ResultDTOUtil.success(taskService.saveTaskInfo(taskToComplete));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO editTask(@PathVariable int id, @Valid TaskForm taskForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        if(notOwner(taskForm.getOwnerId(), request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);

        Optional<TaskInfo> taskToUpdate = taskService.findTaskInfoByIdAndUserId(id, taskForm.getOwnerId());

        if(!taskToUpdate.isPresent()) return new ResultDTO<>(ResultEnum.PARAMS_ERROR);

        if(!taskToUpdate.get().getCategoryId().equals(taskForm.getCategoryId()) &&
                !categoryService.findById(taskForm.getCategoryId()).isPresent()) {
                return ResultDTOUtil.error(ResultEnum.CATEGORY_ENTRY_NOT_FOUND);
        }

        TaskInfo taskInfo = TaskForm2TaskInfoConverter.convertWithOldData(taskForm, taskToUpdate.get());
        return ResultDTOUtil.success(taskService.saveTaskInfo(taskInfo));
    }

    @DeleteMapping("/delete")
    public ResultDTO deleteTask(@RequestParam int tid, @RequestParam int oid, HttpServletRequest request){

        if(notOwner(oid, request))
            return ResultDTOUtil.error(ResultEnum.TASK_PERMISSION_ERROR);

        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(!result.isPresent())
            return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);

        taskService.deleteTaskByIdAndUserId(tid,oid);
        return ResultDTOUtil.success();
    }

    static boolean notOwner(@PathVariable int id, HttpServletRequest request) {
        String userId = request.getHeader(HEADER_USER_ID);
        return !String.valueOf(id).equals(userId);
    }
}
