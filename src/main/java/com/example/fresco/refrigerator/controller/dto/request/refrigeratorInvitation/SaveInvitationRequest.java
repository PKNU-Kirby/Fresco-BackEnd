package com.example.fresco.refrigerator.controller.dto.request.refrigeratorInvitation;

public record SaveInvitationRequest(
        Long refrigeratorId,
        String refrigeratorName,
        Long inviterId,
        String inviterName
) {
}