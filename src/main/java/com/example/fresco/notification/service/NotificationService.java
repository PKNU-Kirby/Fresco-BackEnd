package com.example.fresco.notification.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.NotificationErrorCode;
import com.example.fresco.notification.controller.dto.request.CustomNotificationRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public String sendCustomNotification(String fcmToken, CustomNotificationRequest customNotificationRequest) {
        // fcmToken 유효성 검증

        try {
            String messageId = sendNotification(
                    fcmToken,
                    customNotificationRequest.title(),
                    customNotificationRequest.body()
            );

            log.info("푸시 알림 발송 성공: {}", messageId);
            return "푸시 알림 발송 성공";

        } catch (FirebaseMessagingException e) {
            log.error("푸시 알림 발송 실패: {}", e.getMessage());
            throw new RestApiException(NotificationErrorCode.NOTIFICATION_SEND_FAILED);
        }
    }

    public String sendNotification(String fcmToken, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return firebaseMessaging.send(message);
    }
}
