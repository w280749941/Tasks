package com.heartiger.task.repository;

import com.heartiger.task.datamodel.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskInfoRepository extends JpaRepository<TaskInfo, Integer> {

    Optional<TaskInfo> findByTIdAndOwnerId(Integer taskId, Integer ownerId);

    List<TaskInfo> findByOwnerId(Integer ownerId);

    List<TaskInfo> findByCategoryIdAndOwnerId(Integer categoryId, Integer ownerId);

    void deleteByTIdAndOwnerId(Integer tId, Integer oId);

    void deleteByCategoryIdAndOwnerId(Integer categoryId, Integer userId);
}
