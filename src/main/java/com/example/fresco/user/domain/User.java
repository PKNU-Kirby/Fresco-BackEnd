package com.example.fresco.user.domain;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.global.domain.BaseEntity;
import jakarta.persistence.*;
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
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Column(length = 20)
    private String name;

    @Builder
    public User(Provider provider, String providerId, String name) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
    }

    public static User of(Provider provider, String providerId, String name) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .name(name)
                .build();
    }
}