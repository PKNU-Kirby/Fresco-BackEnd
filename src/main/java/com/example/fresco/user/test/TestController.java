package com.example.fresco.user.test;

import com.example.fresco.auth.controller.dto.request.UserIdRequest;
import com.example.fresco.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login/test")
@RequiredArgsConstructor
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{testUserId}")
    public String getTestUserAccessToken(
            @PathVariable Long testUserId
    ) {
        return jwtTokenProvider.generateAccessToken(new UserIdRequest(testUserId));
    }
}
