package com.heartiger.task.service.impl;

import com.heartiger.task.datamodel.TaskInfo;
import com.heartiger.task.repository.TaskInfoRepository;
import com.heartiger.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskInfoRepository taskInfoRepository;

    @Autowired
    public TaskServiceImpl(TaskInfoRepository taskInfoRepository) {
        this.taskInfoRepository = taskInfoRepository;
    }

    @Override
    public Optional<TaskInfo> findTaskInfoByIdAndUserId(Integer taskId, Integer ownerId) {
        return taskInfoRepository.findByTIdAndOwnerId(taskId, ownerId);
    }

    @Override
    public List<TaskInfo> findAllTasksByOwnerId(Integer ownerId) {
        return taskInfoRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public TaskInfo saveTaskInfo(TaskInfo taskInfo) {
        return taskInfoRepository.save(taskInfo);
    }

    @Override
    @Transactional
    public void deleteTaskByIdAndUserId(Integer taskId, Integer ownerId) {
        taskInfoRepository.deleteByTIdAndOwnerId(taskId, ownerId);
    }
}
