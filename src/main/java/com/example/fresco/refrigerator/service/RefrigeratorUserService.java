package com.example.fresco.refrigerator.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.example.fresco.global.response.error.RefrigeratorErrorCode;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.RefrigeratorIdRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorUser.RefrigeratorUserRequest;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorGroupMemberResponse;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorUser;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorUserRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefrigeratorUserService {
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RefrigeratorGroupMemberResponse> getAllRefrigeratorGroupMember(RefrigeratorIdRequest request) {
        return refrigeratorUserRepository.findAllUsersByRefrigeratorId(request.refrigeratorId());
    }

    @Transactional
    public RefrigeratorInfoResponse approveRefrigeratorInvitation(RefrigeratorUserRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(request.refrigeratorId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        refrigeratorUserRepository.save(new RefrigeratorUser(refrigerator, user));
        return new RefrigeratorInfoResponse(refrigerator.getId(), refrigerator.getName());
    }

    @Transactional
    public String deleteUserToRefrigerator(RefrigeratorUserRequest request) {
        RefrigeratorUser refrigeratorUser = refrigeratorUserRepository.findByRefrigeratorIdAndUserId(request.refrigeratorId(), request.userId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NOT_USER_OF_REFRIGERATOR));

        refrigeratorUserRepository.delete(refrigeratorUser);
        return "성공적으로 삭제하였습니다.";
    }
}
