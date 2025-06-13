package fresco.com.auth.domain;

import fresco.com.member.domain.Member;
import fresco.com.member.domain.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements OAuth2User {
    private Long userId;
    private String name;
    private String email;
    private Collection<MemberRole> authorities;

    public CustomUserDetails(Long userId, String name, String email, Collection<MemberRole> authorities) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    public static CustomUserDetails from(Member member){
        return new CustomUserDetails(member.getId(), member.getName(), member.getEmail(), member.getRoles());
    }

    public Map<String, Object> getAttributes() {
        return Map.of(
                "userId", userId,
                "name", name,
                "email", email,
                "authorities", authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
