package fresco.com.member.domain;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    USER("사용자");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

    public String getDescription() {
        return description;
    }
}