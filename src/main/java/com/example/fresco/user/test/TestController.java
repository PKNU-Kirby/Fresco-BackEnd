package com.example.fresco.user.test;

import com.example.fresco.auth.controller.dto.request.UserIdRequest;
import com.example.fresco.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{testUserId}")
    public String getTestUserAccessToken(
            @PathVariable Long testUserId
    ) {
        log.info("testUserId: {}", testUserId);
        return jwtTokenProvider.generateAccessToken(new UserIdRequest(testUserId));
    }
}
