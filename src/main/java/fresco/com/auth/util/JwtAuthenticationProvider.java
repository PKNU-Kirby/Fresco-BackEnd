package fresco.com.auth.util;

import fresco.com.auth.domain.CustomUserDetails;
import fresco.com.auth.domain.JwtAuthenticationToken;
import fresco.com.auth.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = authentication.getCredentials().toString();
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        CustomUserDetails customUserDetails = customUserDetailService.loadUserByUserId(userId);
        return new JwtAuthenticationToken(customUserDetails, accessToken);
    }

    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}