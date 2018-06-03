package com.heartiger.task.service;

import com.heartiger.task.datamodel.TaskInfo;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskInfo saveTaskInfo(TaskInfo taskInfo);

    Optional<TaskInfo> findTaskInfoByIdAndUserId(Integer taskId, Integer ownerId);

    List<TaskInfo> findAllTasksByOwnerId(Integer ownerId);

    void deleteTaskByIdAndUserId(Integer taskId, Integer ownerId);
}
