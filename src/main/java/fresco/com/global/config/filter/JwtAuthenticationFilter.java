package fresco.com.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import fresco.com.auth.domain.JwtAuthenticationToken;
import fresco.com.auth.util.JwtAuthenticationProvider;
import fresco.com.auth.util.JwtTokenProvider;
import fresco.com.global.exception.RestApiException;
import fresco.com.global.response.error.AuthErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = jwtTokenProvider.extractAccessToken(request);
            authenticateWithAccessToken(accessToken);
            filterChain.doFilter(request, response);
        } catch (RestApiException ex) {
            Map<String, Object> errorResponse = createErrorResponse(response, ex);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void authenticateWithAccessToken(String accessToken) {
        if (accessToken != null) {
            if (jwtTokenProvider.isExpiredToken(accessToken)) {
                throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN, "만료된 토큰입니다.");
            }

            Authentication authentication = jwtAuthenticationProvider.authenticate(new JwtAuthenticationToken(accessToken));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private Map<String, Object> createErrorResponse(HttpServletResponse response, RestApiException ex) {
        response.setStatus(ex.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        return Map.of(
                "errorCode", ex.getErrorCode().getDevelopCode(),
                "errorDescription", ex.getErrorCode().getErrorDescription(),
                "details", ex.getMessage(),
                "errors", ex.getErrors()
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login");
    }
}