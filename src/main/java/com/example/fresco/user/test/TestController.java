package com.example.fresco.user.test;

import com.example.fresco.auth.controller.dto.request.UserIdRequest;
import com.example.fresco.auth.util.JwtTokenProvider;
import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login/test")
@RequiredArgsConstructor
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public String getTestUserAccessToken() {
        return jwtTokenProvider.generateAccessToken(new UserIdRequest(1L));
    }
}
