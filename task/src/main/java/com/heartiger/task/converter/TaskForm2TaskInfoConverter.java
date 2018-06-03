package com.heartiger.task.converter;

import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.form.TaskForm;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class TaskForm2TaskInfoConverter {

    /**
     *  Add categoryForm data to a new CategoryInfo
     */
    public static TaskInfo convert(TaskForm taskForm) {

        TaskInfo taskInfo = new TaskInfo();
        convertWithTaskInfo(taskForm, taskInfo);

        return taskInfo;
    }

    /**
     * Add categoryForm data to CategoryInfo from database.
     */
    public static TaskInfo convertWithOldData(TaskForm taskForm, TaskInfo taskToUpdate) {

        TaskInfo taskInfo = new TaskInfo();
        BeanUtils.copyProperties(taskToUpdate, taskInfo);
        taskInfo = convertWithTaskInfo(taskForm, taskInfo);
        taskInfo.setUpdatedTime(new Date());

        return taskInfo;
    }

    private static TaskInfo convertWithTaskInfo(TaskForm taskForm, TaskInfo taskInfo) {

        taskInfo.setTitle(taskForm.getTitle());
        taskInfo.setDescription(taskForm.getDescription());
        taskInfo.setPriority(taskForm.getPriority());
        taskInfo.setOwnerId(taskForm.getOwnerId());
        taskInfo.setCategoryId(taskForm.getCategoryId());

        if(taskForm.getDueTime() != null) taskInfo.setDueTime(taskForm.getDueTime());
        if(taskForm.getReminderTime() != null) taskInfo.setReminderTime(taskForm.getReminderTime());
        if(taskForm.getIsCompleted() != null) taskInfo.setIsCompleted(taskForm.getIsCompleted());
        if(taskForm.getIsDeleted() != null) taskInfo.setIsDeleted(taskForm.getIsDeleted());

        return taskInfo;
    }
}
