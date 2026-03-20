package com.example.fresco.refrigerator.domain;

import lombok.Getter;

@Getter
public enum InvitationStatus {
    PENDING("초대 중", "PENDING"),
    ACCEPTED("수락", "ACCEPTED"),
    REJECTED("거절", "REJECTED");

    private final String description;
    private final String code;

    InvitationStatus(String description, String code) {
        this.description = description;
        this.code = code;
    }

    // 코드로 ENUM 찾기
    public static InvitationStatus fromCode(String code) {
        for (InvitationStatus status : InvitationStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid invitation status code: " + code);
    }

    // 설명으로 ENUM 찾기
    public static InvitationStatus fromDescription(String description) {
        for (InvitationStatus status : InvitationStatus.values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid invitation status description: " + description);
    }

    // 초대가 완료된 상태인지 확인
    public boolean isCompleted() {
        return this == ACCEPTED || this == REJECTED;
    }

    // 초대 대기 중인지 확인
    public boolean isPending() {
        return this == PENDING;
    }

    @Override
    public String toString() {
        return description;
    }
}