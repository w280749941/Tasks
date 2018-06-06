package com.heartiger.task.converter;

import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.form.TaskForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TaskForm2TaskInfoConverterTest {

    private TaskForm taskFormSource;

    private TaskInfo taskInfoToUpdate;
    private TaskInfo taskInfoExpected;

    @Before
    public void setup() {

        taskFormSource = new TaskForm();
        taskFormSource.setDescription("Task description");
        taskFormSource.setTitle("Task title");
        taskFormSource.setIsCompleted(true);
        taskFormSource.setIsDeleted(true);
        taskFormSource.setPriority(3);
        taskFormSource.setCategoryId(5);
        taskFormSource.setOwnerId(6);

        taskInfoToUpdate = new TaskInfo();
        taskInfoToUpdate.setDescription("666556");
        taskInfoToUpdate.setTitle("5345354");
        taskInfoToUpdate.setIsCompleted(false);
        taskInfoToUpdate.setIsDeleted(false);
        taskInfoToUpdate.setPriority(13);
        taskInfoToUpdate.setCategoryId(15);
        taskInfoToUpdate.setOwnerId(62);

        taskInfoExpected = new TaskInfo();
        taskInfoExpected.setDescription("Task description");
        taskInfoExpected.setTitle("Task title");
        taskInfoExpected.setIsCompleted(true);
        taskInfoExpected.setIsDeleted(true);
        taskInfoExpected.setPriority(3);
        taskInfoExpected.setCategoryId(5);
        taskInfoExpected.setOwnerId(6);

    }


    @Test
    public void convertTaskFormToTaskInfoShouldMatchExpected() {
        TaskInfo taskInfoDest = TaskForm2TaskInfoConverter.convert(taskFormSource);
        Assert.assertEquals(taskInfoExpected, taskInfoDest);
    }

    @Test
    public void convertTaskFormToAnExistingTaskInfoWithOldDataShouldMatchExpected() {
        TaskInfo taskInfoDest = TaskForm2TaskInfoConverter.convertWithOldData(taskFormSource, taskInfoToUpdate);

        // Since function adds an update time, add it to expected as well
        taskInfoExpected.setUpdatedTime(taskInfoDest.getUpdatedTime());
        Assert.assertEquals(taskInfoExpected, taskInfoDest);
    }
}