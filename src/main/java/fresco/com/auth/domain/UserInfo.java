package fresco.com.auth.domain;

import fresco.com.user.domain.User;
import lombok.Getter;

@Getter
public class UserInfo {
    private Long userId;
    private Provider provider;
    private String providerId;
    private String name;

    public UserInfo(Long userId, Provider provider, String providerId, String name) {
        this.userId = userId;
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
    }

    public static UserInfo from(User user){
        return new UserInfo(user.getId(), user.getProvider(), user.getProviderId(), user.getName());
    }
}
