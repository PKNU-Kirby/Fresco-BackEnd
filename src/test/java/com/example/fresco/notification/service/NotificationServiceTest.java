package com.example.fresco.notification.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.notification.controller.dto.request.CustomNotificationRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void 커스텀_알림_전송_성공_테스트() throws FirebaseMessagingException {
        // Given
        String fcmToken = "valid-token";
        CustomNotificationRequest request = new CustomNotificationRequest("제목", "내용");
        String expectedMessageId = "projects/test/messages/12345";

        when(firebaseMessaging.send(any(Message.class)))
                .thenReturn(expectedMessageId);

        // When
        String result = notificationService.sendCustomNotification(fcmToken, request);

        // Then
        assertEquals("푸시 알림 발송 성공", result);
        verify(firebaseMessaging, times(1)).send(any(Message.class));
    }

    @Test
    void 잘못된_토큰_예외_처리_테스트() throws FirebaseMessagingException {
        // Given
        String invalidToken = "invalid-token";
        CustomNotificationRequest request = new CustomNotificationRequest("제목", "내용");

        when(firebaseMessaging.send(any(Message.class)))
                .thenThrow(FirebaseMessagingException.class);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            notificationService.sendCustomNotification(invalidToken, request);
        });

        assertEquals("NOTIFICATION_ERR_001", exception.getErrorCode().getDevelopCode());
        verify(firebaseMessaging, times(1)).send(any(Message.class));
    }
}