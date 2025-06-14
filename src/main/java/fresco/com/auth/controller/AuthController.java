package fresco.com.auth.controller;

import fresco.com.auth.controller.dto.request.LoginRequest;
import fresco.com.auth.controller.dto.response.LoginResponse;
import fresco.com.auth.service.AuthService;
import fresco.com.global.response.SuccessResponse;
import fresco.com.global.response.success.AuthSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public SuccessResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return SuccessResponse.of(AuthSuccessCode.LOGIN_SUCCESS, authService.login(req));
    }
}
