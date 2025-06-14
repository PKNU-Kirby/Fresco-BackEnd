package fresco.com.auth.util;

import fresco.com.auth.domain.JwtAuthenticationToken;
import fresco.com.auth.domain.UserInfo;
import fresco.com.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider  {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = authentication.getCredentials().toString();
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        UserInfo userInfo = userRepository.findUserInfoById(userId);
        return new JwtAuthenticationToken(userInfo, accessToken);
    }
}