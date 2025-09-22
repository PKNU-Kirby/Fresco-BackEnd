package com.example.fresco.global.config.filter;

import com.example.fresco.auth.domain.JwtAuthenticationToken;
import com.example.fresco.auth.util.JwtAuthenticationProvider;
import com.example.fresco.auth.util.JwtTokenProvider;
import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            log.info("JWT Filter - Processing request: {}", requestURI);

            if (shouldNotFilter(request)) {
                log.debug("JWT Filter - Skipping authentication for: {}", requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            log.info("JWT Filter - Authorization header: {}", authHeader);

            String accessToken = jwtTokenProvider.extractAccessToken(request);
            log.info("JWT Filter - Extracted token: {}", accessToken != null ? "Present" : "Null");

            authenticateWithAccessToken(accessToken);
            log.info("JWT Filter - Authentication successful for: {}", requestURI);

            filterChain.doFilter(request, response);
        } catch (RestApiException ex) {
            log.error("JWT Filter - Authentication failed: {}", ex.getMessage());
            Map<String, Object> errorResponse = createErrorResponse(response, ex);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void authenticateWithAccessToken(String accessToken) {
        if (accessToken != null) {
            log.debug("JWT Filter - Validating token");

            if (jwtTokenProvider.isExpiredToken(accessToken)) {
                log.error("JWT Filter - Token is expired");
                throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN, "만료된 토큰입니다.");
            }

            log.debug("JWT Filter - Token is valid, authenticating");
            Authentication authentication = jwtAuthenticationProvider.authenticate(new JwtAuthenticationToken(accessToken));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("JWT Filter - Authentication set in SecurityContext");
        } else {
            log.error("JWT Filter - No access token provided");
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN, "토큰이 필요합니다.");
        }
    }

    private Map<String, Object> createErrorResponse(HttpServletResponse response, RestApiException ex) {
        response.setStatus(ex.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", ex.getErrorCode().getDevelopCode());
        errorResponse.put("errorDescription", ex.getErrorCode().getErrorDescription());
        errorResponse.put("details", ex.getMessage() != null ? ex.getMessage() : "Unknown error");
        errorResponse.put("errors", ex.getErrors() != null ? ex.getErrors() : new ArrayList<>());
        return errorResponse;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login") ||
               path.startsWith("/api/v1/login/test") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-resources/");
    }
}