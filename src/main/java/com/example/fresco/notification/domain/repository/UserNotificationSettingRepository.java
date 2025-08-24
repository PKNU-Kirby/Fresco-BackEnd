package com.example.fresco.notification.domain.repository;

import com.example.fresco.notification.domain.UserNotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {
    @Query("select uns.user.id from UserNotificationSetting as uns where uns.pushEnabled = true")
    List<Long> findByPushNotificationEnabled();
}
