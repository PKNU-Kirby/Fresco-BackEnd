package com.example.fresco.auth.util;

import com.example.fresco.auth.domain.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = authentication.getCredentials().toString();
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        return new JwtAuthenticationToken(userId, accessToken);
    }
}