package com.heartiger.task_user.repository;

import com.heartiger.task_user.datamodel.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findUserByEmail(String userEmail);
}
