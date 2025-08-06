package com.example.fresco.refrigerator.domain;

import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "refrigeratorInvitation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefrigeratorInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "refrigeratorId", nullable = false)
    private Refrigerator refrigerator;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inviterId", nullable = false)
    private User inviter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inviteeId")
    private User invitee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Builder
    public RefrigeratorInvitation(Refrigerator refrigerator, User inviter, User invitee, InvitationStatus status) {
        this.refrigerator = refrigerator;
        this.inviter = inviter;
        this.invitee = invitee;
        this.status = status;
    }

    public static RefrigeratorInvitation of(Refrigerator invitationRefrigerator, User invitationInviter) {
        return RefrigeratorInvitation.builder()
                .refrigerator(invitationRefrigerator)
                .inviter(invitationInviter)
                .status(InvitationStatus.PENDING)
                .build();
    }
}