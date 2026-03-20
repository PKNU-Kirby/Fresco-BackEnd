package com.example.fresco.notification.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.NotificationSuccessCode;
import com.example.fresco.notification.controller.dto.request.CustomNotificationRequest;
import com.example.fresco.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class notificationController {
    private final NotificationService notificationService;

    @PostMapping
    public SuccessResponse<String> sendCustomNotification(
            @RequestBody CustomNotificationRequest customNotificationRequest,
            @RequestHeader(value = "X-FCM-Token") String fcmToken
    ) {
        return SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_PUSH_SUCCESS,
                notificationService.sendCustomNotification(fcmToken, customNotificationRequest));
    }
}