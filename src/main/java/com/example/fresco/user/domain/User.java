package com.example.fresco.user.domain;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_provider_id",
                columnNames = {"provider", "providerId"}))
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Email
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Column(length = 20)
    private String name;

    private String fcmToken;

    @Builder
    public User(Provider provider, String providerId, String userName, String fcmToken) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = userName;
        this.fcmToken = fcmToken;
    }

    public static User of(Provider provider, String providerId, String userName) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .userName(userName)
                .build();
    }
}