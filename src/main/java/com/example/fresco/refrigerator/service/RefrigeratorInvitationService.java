package com.example.fresco.refrigerator.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.RefrigeratorErrorCode;
import com.example.fresco.global.response.error.RefrigeratorUserErrorCode;
import com.example.fresco.global.response.error.UserErrorCode;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorInvitation.RefrigeratorInvitationIdRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorInvitation.SaveInvitationRequest;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInvitationResponse;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorInvitation;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorInvitationRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefrigeratorInvitationService {
    private final RefrigeratorInvitationRepository refrigeratorInvitationRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefrigeratorInvitationResponse saveInvitationInfo(SaveInvitationRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(request.refrigeratorId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));
        User inviter = userRepository.findById(request.inviterId())
                .orElseThrow(() -> new RestApiException(UserErrorCode.NULL_USER));

        RefrigeratorInvitation savedRefrigeratorInvitation = refrigeratorInvitationRepository.save(RefrigeratorInvitation.of(refrigerator, inviter));
        return RefrigeratorInvitationResponse.from(savedRefrigeratorInvitation);
    }

    @Transactional(readOnly = true)
    public RefrigeratorInvitationResponse getInvitationInfo(RefrigeratorInvitationIdRequest request) {
        return refrigeratorInvitationRepository.findInvitationInfoById(request.refrigeratorInvitationId())
                .orElseThrow(() -> new RestApiException(RefrigeratorUserErrorCode.NULL_REFRIGERATOR_USER));
    }
}
