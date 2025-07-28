package com.example.fresco.refrigerator.domain;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refrigeratorUsers",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_refrigerator_user",
                columnNames = {"refrigeratorId", "userId"}))
public class RefrigeratorUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "refrigeratorId", nullable = false)
    private Refrigerator refrigerator;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public RefrigeratorUser(Refrigerator refrigerator, User user) {
        this.refrigerator = refrigerator;
        this.user = user;
    }
}