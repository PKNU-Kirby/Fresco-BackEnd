package fresco.com.auth.controller;

import fresco.com.auth.controller.dto.request.LoginRequest;
import fresco.com.auth.controller.dto.request.RefreshTokenRequest;
import fresco.com.auth.controller.dto.request.UserIdRequest;
import fresco.com.auth.controller.dto.response.LoginSuccessResponse;
import fresco.com.auth.domain.UserInfo;
import fresco.com.auth.service.AuthService;
import fresco.com.global.response.SuccessResponse;
import fresco.com.global.response.success.AuthSuccessCode;
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
    public SuccessResponse<LoginSuccessResponse> logout(@AuthenticationPrincipal UserInfo userInfo) {
        return SuccessResponse.of(AuthSuccessCode.LOGOUT_SUCCESS, authService.logout(new UserIdRequest(userInfo.getUserId())));
    }
}
