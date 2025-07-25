package com.example.fresco.notification.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false)
    private String message;

    private Boolean isRead = false;
}