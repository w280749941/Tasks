package com.heartiger.task.repository;

import com.heartiger.task.datamodel.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Integer> {

    List<CategoryInfo> findByOwnerId(Integer ownerId);
}
