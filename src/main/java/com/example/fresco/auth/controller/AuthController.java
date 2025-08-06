package com.example.fresco.auth.controller;

import com.example.fresco.auth.controller.dto.request.LoginRequest;
import com.example.fresco.auth.controller.dto.request.RefreshTokenRequest;
import com.example.fresco.auth.controller.dto.request.UserIdRequest;
import com.example.fresco.auth.controller.dto.response.LoginSuccessResponse;
import com.example.fresco.auth.service.AuthService;
import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.AuthSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<LoginSuccessResponse> login(@Valid @RequestBody LoginRequest request) {
        return SuccessResponse.of(AuthSuccessCode.LOGIN_SUCCESS, authService.login(request));
    }

    @PostMapping("/refresh")
    public SuccessResponse<LoginSuccessResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return SuccessResponse.of(AuthSuccessCode.REISSUE_TOKEN_SUCCESS, authService.refreshAuthToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public SuccessResponse<LoginSuccessResponse> logout(@AuthenticationPrincipal Long userId) {
        return SuccessResponse.of(AuthSuccessCode.LOGOUT_SUCCESS, authService.logout(new UserIdRequest(userId)));
    }
}
