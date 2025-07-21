package fresco.com.user.domain;

import fresco.com.auth.domain.Provider;
import fresco.com.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;
    private String name;
    private String fcmToken;

    @Builder
    public User(Provider provider, String providerId, String name, String fcmToken) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.fcmToken = fcmToken;
    }

    public static User of(Provider provider, String providerId, String name) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .name(name)
                .build();
    }
}