package com.example.fresco.user.domain.repository;

import com.example.fresco.user.domain.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    List<UserFcmToken> findAllByUserIdIn(List<Long> userIds);
}
