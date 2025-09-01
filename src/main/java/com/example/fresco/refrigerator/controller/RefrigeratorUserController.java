package com.example.fresco.refrigerator.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RefrigeratorSuccessCode;
import com.example.fresco.global.response.success.RefrigeratorUserSuccessCode;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.RefrigeratorIdRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigeratorUser.RefrigeratorUserRequest;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorGroupMemberResponse;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import com.example.fresco.refrigerator.service.RefrigeratorUserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refrigerator/users")
public class RefrigeratorUserController {
    private final RefrigeratorUserService refrigeratorUserService;

    @GetMapping("/{refrigeratorId}")
    public SuccessResponse<List<RefrigeratorGroupMemberResponse>> getRefrigeratorGroupMember(
            @NotNull @PathVariable Long refrigeratorId
    ) {
        return SuccessResponse.of(RefrigeratorUserSuccessCode.REFRIGERATOR_USER_GET_SUCCESS,
                refrigeratorUserService.getAllRefrigeratorGroupMember(new RefrigeratorIdRequest(refrigeratorId)));
    }

    @PostMapping("/{refrigeratorId}")
    public SuccessResponse<RefrigeratorInfoResponse> approveRefrigeratorInvitation(
            @AuthenticationPrincipal Long userId,
            @NotNull @PathVariable Long refrigeratorId
    ) {
        return SuccessResponse.of(RefrigeratorUserSuccessCode.REFRIGERATOR_USER_ADD_SUCCESS,
                refrigeratorUserService.approveRefrigeratorInvitation(new RefrigeratorUserRequest(userId, refrigeratorId)));
    }

    @DeleteMapping("/{refrigeratorId}/{deleteUserId}")
    public SuccessResponse<String> deleteUserToRefrigerator(
            @NotNull @PathVariable Long deleteUserId,
            @NotNull @PathVariable Long refrigeratorId
    ) {
        return SuccessResponse.of(RefrigeratorUserSuccessCode.REFRIGERATOR_USER_DELETE_SUCCESS,
                refrigeratorUserService.deleteUserToRefrigerator(new RefrigeratorUserRequest(deleteUserId, refrigeratorId)));
    }
}