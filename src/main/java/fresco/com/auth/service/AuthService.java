package fresco.com.auth.service;

import fresco.com.auth.controller.dto.request.LoginRequest;
import fresco.com.auth.controller.dto.request.RefreshTokenRequest;
import fresco.com.auth.controller.dto.request.UserIdRequest;
import fresco.com.auth.controller.dto.response.LoginSuccessResponse;
import fresco.com.auth.controller.dto.response.SocialResponse;
import fresco.com.auth.domain.RefreshToken;
import fresco.com.auth.domain.repository.RefreshTokenRepository;
import fresco.com.auth.infra.KakaoApiClient;
import fresco.com.auth.infra.NaverApiClient;
import fresco.com.auth.util.JwtTokenProvider;
import fresco.com.global.exception.RestApiException;
import fresco.com.global.response.error.AuthErrorCode;
import fresco.com.user.domain.User;
import fresco.com.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public LoginSuccessResponse login(@Valid LoginRequest req) {
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
        return new LoginSuccessResponse(accessToken, refreshToken);
    }

    @Transactional
    public LoginSuccessResponse refreshAuthToken(@Valid RefreshTokenRequest request){
        if(jwtTokenProvider.isExpiredToken(request.refreshToken())){
            throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(request.refreshToken());
        User user = getUserById(userId);

        String accessToken = jwtTokenProvider.generateAccessToken(new UserIdRequest(user.getId()));
        String refreshToken = generateRefreshToken(user);
        return new LoginSuccessResponse(accessToken, refreshToken);
    }

    @Transactional
    public LoginSuccessResponse logout(@Valid UserIdRequest loginUserRequest) {
        User user = getUserById(loginUserRequest.userId());
        refreshTokenRepository.deleteAllByUser(user);
        return new LoginSuccessResponse(null, null);
    }

    private User getUserById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));
    }

    private String generateRefreshToken(User user) {
        RefreshToken token = new RefreshToken(user);

        if(refreshTokenRepository.findByUserId(user.getId()).isPresent()) {
            refreshTokenRepository.deleteAllByUser(user);
        }

        refreshTokenRepository.save(token);
        String refreshToken = jwtTokenProvider.generateRefreshToken(new UserIdRequest(user.getId()), token.getId());

        token.putRefreshToken(refreshToken);
        return refreshToken;
    }
}