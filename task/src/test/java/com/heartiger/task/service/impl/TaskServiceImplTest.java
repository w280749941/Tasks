package com.heartiger.task.service.impl;

import com.heartiger.task.TaskApplicationTests;
import com.heartiger.task.datamodel.CategoryInfo;
import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.service.CategoryService;
import com.heartiger.task.service.TaskService;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Component
public class TaskServiceImplTest extends TaskApplicationTests {

    @Autowired
    private TaskService taskService;

    private Integer ownerId;
    private Integer taskId;
    private TaskInfo taskInfo;
    private Integer categoryId;

    @Before
    public void setup() {
        categoryId = 5;
        ownerId = 1;
        taskId = 2;
        taskInfo = new TaskInfo();
        taskInfo.setOwnerId(ownerId);
        taskInfo.setTitle("Two Task");
        taskInfo.setDescription("Some description!");
        taskInfo.setPriority(1);
        taskInfo.setCategoryId(categoryId);
        Date dt = new Date();
        DateTime dtOrg = new DateTime(dt);
        taskInfo.setDueTime(dtOrg.plusDays(2).toDate());
        taskInfo.setReminderTime(dtOrg.plusDays(1).toDate());
    }

    @Test
    public void findTaskByIdAndUserIdShouldReturnOne() {
        Optional<TaskInfo> taskInfo = taskService.findTaskInfoByIdAndUserId(taskId, ownerId);
        Assert.assertNotNull(String.format("No Task found with Task Id %d and owner Id %d", taskId, ownerId), taskInfo);
    }

    @Test
    public void findTasksByOwnerIdShouldReturnSizeGreaterThanZero() {
        List<TaskInfo> taskInfoList = taskService.findAllTasksByOwnerId(ownerId);
        Assert.assertTrue(String.format("No task found with owner Id %d", ownerId), taskInfoList.size()>0);
    }

    @Test
    public void findTasksByCategoryIdAndOwnerIdShouldReturnSizeGreaterThanZero() {
        List<TaskInfo> taskInfoList = taskService.findTasksByCategoryIdAndOwnerId(categoryId, ownerId);
        Assert.assertTrue(String.format("No task found with category Id %d and owner Id %d", categoryId, ownerId), taskInfoList.size()>0);
    }

    @Test
    @Transactional
    public void saveTaskInfoShouldReturnOne() {
        TaskInfo returnedTaskInfo = taskService.saveTaskInfo(taskInfo);
        Assert.assertNotNull("Task can't be saved", returnedTaskInfo);
    }

    @Test
    @Transactional
    public void deleteTaskInfoShouldFoundNull() {
        taskService.deleteTaskByIdAndUserId(taskId, ownerId);
        Assert.assertFalse(taskService.findTaskInfoByIdAndUserId(taskId, ownerId).isPresent());
    }

    @Test
    @Transactional
    public void updateTaskInfoShouldReturnOne() {
        Optional<TaskInfo> taskToUpdate = taskService.findTaskInfoByIdAndUserId(taskId,ownerId);
        if(taskToUpdate.isPresent()) {
            taskToUpdate.get().setTitle("update Task");
            taskToUpdate.get().setDescription("update Description");
            Assert.assertNotNull("Task can't be saved", taskService.saveTaskInfo(taskToUpdate.get()));
        }
    }

    @Test
    @Transactional
    public void deleteTasksByIdAndUserIdShouldFoundNull() {
        taskService.deleteTasksByCategoryIdAndUserId(categoryId, ownerId);
        Assert.assertEquals("Tasks can't be deleted",
                taskService.findTasksByCategoryIdAndOwnerId(categoryId, ownerId).size(),
                0);
    }
}