package fresco.com.auth.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserInfo userInfo;
    private String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.userInfo = null;
        this.token = token;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserInfo userInfo, String token) {
        super(null);
        this.userInfo = userInfo;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.userInfo;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        if (authenticated) {
            throw new IllegalArgumentException("옳지 않은 과정을 통해 인증되었습니다.");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
