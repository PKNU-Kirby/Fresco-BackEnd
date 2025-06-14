package fresco.com.auth.service;

import fresco.com.auth.controller.dto.request.LoginRequest;
import fresco.com.auth.controller.dto.request.UserIdRequest;
import fresco.com.auth.controller.dto.response.LoginResponse;
import fresco.com.auth.controller.dto.response.SocialResponse;
import fresco.com.auth.domain.RefreshToken;
import fresco.com.auth.domain.repository.RefreshTokenRepository;
import fresco.com.auth.infra.KakaoApiClient;
import fresco.com.auth.infra.NaverApiClient;
import fresco.com.auth.util.JwtTokenProvider;
import fresco.com.user.domain.User;
import fresco.com.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {

    private final KakaoApiClient kakao;
    private final NaverApiClient naver;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(@Valid LoginRequest req) {
        SocialResponse profile = switch (req.provider()) {
            case KAKAO -> kakao.profile(req.accessToken());
            case NAVER -> naver.profile(req.accessToken());
        };

        User user = userRepository.findByProviderAndProviderId(profile.getProvider(), profile.getProviderId())
                .orElseGet(() -> userRepository.save(profile.toEntity()));
        UserIdRequest userIdRequest = new UserIdRequest(user.getId());

        refreshTokenRepository.findByUserId(user.getId()).
                ifPresent(token -> refreshTokenRepository.delete(token));
        RefreshToken savedToken = refreshTokenRepository.save(new RefreshToken(user));

        String accessToken = jwtTokenProvider.generateAccessToken(userIdRequest);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userIdRequest, savedToken.getId());
        return new LoginResponse(accessToken, refreshToken);
    }
}
