package com.example.fresco.notification.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.NotificationErrorCode;
import com.example.fresco.notification.domain.repository.UserNotificationSettingRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorUserRepository;
import com.example.fresco.user.domain.UserFcmToken;
import com.example.fresco.user.domain.repository.UserFcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientExpirationScheduler {
    private final UserNotificationSettingRepository userNotificationSettingRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final NotificationService notificationService;


    @Scheduled(cron = "0 0 18 * * *") // 매일 오후 6시
    public void sendExpirationNotifications() {
        log.info("식재료 소비기한 알림 작업 시작");

        // 1. 푸시 알림을 허용한 사용자들 조회
        List<Long> pushEnabledUserIds = userNotificationSettingRepository.findByPushNotificationEnabled();

        LocalDate days3Later = LocalDate.now().plusDays(3);


        // 2. 3일 이내 식재료가 있는 냉장고의 사용자 ID 조회
        List<Long> userIdsWithExpiringIngredients = refrigeratorUserRepository.findExpiringIngredientsWithinDays(days3Later);

        // 3. 3일 이내 식재료가 있는 냉장고의 사용자 중에서 푸쉬 알림을 허용한 사용자 id만 추출
        List<Long> notifiableUserIds = userIdsWithExpiringIngredients.stream()
                .filter(userId -> pushEnabledUserIds.contains(userId))
                .toList();

        // 4. 푸쉬 알림을 보낼 사용자들의 fcmToken 정보 가져오기
        List<UserFcmToken> allUserFcmTokens = userFcmTokenRepository.findAllByUserIdIn(notifiableUserIds);

        // 4. 푸시 알림 발송
        for (UserFcmToken userFcmToken : allUserFcmTokens) {
            try {
                notificationService.sendNotification(
                        userFcmToken.getFcmToken(),
                        "식재료 소비기한 알림",
                        "소비기한이 3일 이내인 식재료가 존재합니다."
                );

                log.info("사용자 {}에게 만료 알림 발송", userFcmToken.getUser().getId());
            } catch (FirebaseMessagingException e) {
                log.info("식재료 소비기한 알림 전송 실패, 사용자 ID :{}", userFcmToken.getUser().getId());
                throw new RestApiException(NotificationErrorCode.NOTIFICATION_SEND_FAILED);
            }
        }

        log.info("식재료 소비기한 알림 작업 완료");
    }
}