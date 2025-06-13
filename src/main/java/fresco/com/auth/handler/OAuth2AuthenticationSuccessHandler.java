package fresco.com.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fresco.com.auth.domain.CustomUserDetails;
import fresco.com.auth.domain.RefreshToken;
import fresco.com.auth.domain.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import fresco.com.auth.domain.repository.RefreshTokenRepository;
import fresco.com.auth.dto.request.AuthTokenRequest;
import fresco.com.auth.dto.request.UserDetailRequest;
import fresco.com.auth.util.CookieUtils;
import fresco.com.auth.util.JwtTokenProvider;
import fresco.com.global.exception.RestApiException;
import fresco.com.global.properties.JwtProperties;
import fresco.com.global.response.error.AuthErrorCode;
import fresco.com.member.domain.Member;
import fresco.com.member.domain.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fresco.com.auth.domain.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Value("app.oauth2.authorized-redirect-uris")
    List<String> authorizedRedirectUris;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        UserDetailRequest userDetailRequest = new UserDetailRequest(principal.getUserId());
        AuthTokenRequest authToken = generateAndSaveAuthToken(userDetailRequest);
        setResponse(request, response, authToken);

        String targetUrl = determineTargetUrl(request, authToken);
        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋되어 " + targetUrl + "로 리다이렉트할 수 없습니다.");
            return;
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void setResponse(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthTokenRequest authTokenRequest) {
        clearAuthenticationAttributes(request, response);
        Cookie refreshTokenCookie = generateRefreshCookie(authTokenRequest.refreshToken());
        response.addCookie(refreshTokenCookie);
    }

    private Cookie generateRefreshCookie(final String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(jwtProperties.getRefreshTokenValidTime().intValue());
        // https 설정 후 secure 옵션 추가 필요
        return refreshTokenCookie;
    }

    private AuthTokenRequest generateAndSaveAuthToken(final UserDetailRequest userDetailRequest) {
        Member user = memberRepository.findById(userDetailRequest.userId()).orElse(null);

        RefreshToken token = new RefreshToken(user);
        String accessToken = tokenProvider.generateAccessToken(userDetailRequest);
        String refreshToken = tokenProvider.generateRefreshToken(userDetailRequest, token.getId());

        token.putRefreshToken(refreshToken);
        refreshTokenRepository.save(token);
        return new AuthTokenRequest(accessToken, refreshToken);
    }

    protected String determineTargetUrl(HttpServletRequest request, AuthTokenRequest authTokenRequest) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RestApiException(AuthErrorCode.INVALID_REDIRECT_URI);
        }

        String targetUrl = redirectUri.orElse("http://localhost:3000");
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", authTokenRequest.accessToken())
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(final String uri) {
        URI clientRedirectUri = URI.create(uri);

        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (clientRedirectUri.equals(authorizedURI) || authorizedRedirectUri.equals("*")) {
                        return true;
                    }
                    return false;
                });
    }

    protected void clearAuthenticationAttributes(final HttpServletRequest request, final HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}