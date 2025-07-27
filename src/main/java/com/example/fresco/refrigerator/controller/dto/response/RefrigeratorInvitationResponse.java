package com.example.fresco.refrigerator.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorInvitation;
import lombok.Builder;

@Builder
public record RefrigeratorInvitationResponse(
        Long refrigeratorInvitationId,
        Long refrigeratorId,
        String refrigeratorName,
        Long inviterId,
        String inviterName
) {
    public static RefrigeratorInvitationResponse from(RefrigeratorInvitation refrigeratorInvitation) {
        return RefrigeratorInvitationResponse.builder()
                .refrigeratorInvitationId(refrigeratorInvitation.getId())
                .refrigeratorId(refrigeratorInvitation.getId())
                .refrigeratorName(refrigeratorInvitation.getRefrigerator().getName())
                .inviterId(refrigeratorInvitation.getInvitee().getId())
                .inviterName(refrigeratorInvitation.getInvitee().getName())
                .build();
    }
}