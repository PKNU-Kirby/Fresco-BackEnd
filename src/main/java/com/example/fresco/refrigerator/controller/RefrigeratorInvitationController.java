package com.example.fresco.refrigerator.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RefrigeratorInvitationSuccessCode;
import com.example.fresco.global.response.success.RefrigeratorSuccessCode;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorInvitation.RefrigeratorInvitationIdRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorInvitation.SaveInvitationRequest;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInvitationResponse;
import com.example.fresco.refrigerator.service.RefrigeratorInvitationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/refrigerator/invitation")
@RequiredArgsConstructor
public class RefrigeratorInvitationController {
    private final RefrigeratorInvitationService refrigeratorInvitationService;

    /**
     * 냉장고 초대 정보를 저장합니다.
     *
     * @param saveInvitationRequest 냉장고 초대 요청 정보 (냉장고 ID, 냉장고 이름, 초대자 ID, 초대자 이름)
     * @return 저장된 냉장고 초대 응답 정보 (초대 ID, 냉장고 ID, 냉장고 이름, 피초대자 ID, 피초대자 이름)
     */
    @PostMapping
    public SuccessResponse<RefrigeratorInvitationResponse> saveInvitationInfo(
            @NotNull SaveInvitationRequest saveInvitationRequest
    ) {
        return SuccessResponse.of(RefrigeratorInvitationSuccessCode.SAVE_INVITATION_SUCCESS,
                refrigeratorInvitationService.saveInvitationInfo(saveInvitationRequest));
    }

    /**
     * 냉장고 초대 정보를 가져옵니다.
     *
     * @param refrigeratorInvitationId 냉장고 초대 ID
     * @return 저장된 냉장고 초대 응답 정보 (초대 ID, 냉장고 ID, 냉장고 이름, 피초대자 ID, 피초대자 이름)
     */
    @GetMapping("/{refrigeratorInvitationId}")
    public SuccessResponse<RefrigeratorInvitationResponse> getInvitationInfo(
            @NotNull @PathVariable Long refrigeratorInvitationId
    ) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_LIST_SUCCESS,
                refrigeratorInvitationService.getInvitationInfo(new RefrigeratorInvitationIdRequest(refrigeratorInvitationId)));
    }
}
