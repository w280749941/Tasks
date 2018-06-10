package com.heartiger.task.controller;

import com.heartiger.task.converter.TaskForm2TaskInfoConverter;
import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.dto.ResultDTO;
import com.heartiger.task.enums.ResultEnum;
import com.heartiger.task.exception.TasksException;
import com.heartiger.task.form.TaskForm;
import com.heartiger.task.service.CategoryService;
import com.heartiger.task.service.TaskService;
import com.heartiger.task.utils.ResultDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResultDTO<Object> getAllTasks(@PathVariable int id){
        return ResultDTOUtil.success(taskService.findAllTasksByOwnerId(id));
    }

    @GetMapping("/search")
    public ResultDTO findTaskByIdAndUserId(@RequestParam int tid, @RequestParam int oid) {
        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(result.isPresent()) {
            return ResultDTOUtil.success(result.get());
        }
        return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);
    }

    @PostMapping("/new")
    public ResultDTO<Object> createTask(@Valid TaskForm taskForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());

        TaskInfo taskInfo = TaskForm2TaskInfoConverter.convert(taskForm);
        return ResultDTOUtil.success(taskService.saveTaskInfo(taskInfo));
    }

    @PostMapping("/complete")
    public ResultDTO<Object> completeTask(@RequestParam int tid, @RequestParam int oid) {

        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(!result.isPresent())
            ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);

        TaskInfo taskToComplete = result.get();
        taskToComplete.setIsCompleted(true);
        return ResultDTOUtil.success(taskService.saveTaskInfo(taskToComplete));
    }

    @PostMapping("/edit/{id}")
    public ResultDTO editTask(@PathVariable int id, @Valid TaskForm taskForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new TasksException(ResultEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
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
    public ResultDTO deleteTask(@RequestParam int tid, @RequestParam int oid){
        Optional<TaskInfo> result = taskService.findTaskInfoByIdAndUserId(tid, oid);
        if(!result.isPresent())
            return ResultDTOUtil.error(ResultEnum.ENTRY_NOT_FOUND);

        taskService.deleteTaskByIdAndUserId(tid,oid);
        return ResultDTOUtil.success();
    }
}
