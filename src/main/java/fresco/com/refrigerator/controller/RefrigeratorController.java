package fresco.com.refrigerator.controller;

import fresco.com.global.response.SuccessResponse;
import fresco.com.global.response.success.RefrigeratorSuccessCode;
import fresco.com.refrigerator.controller.dto.request.*;
import fresco.com.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import fresco.com.refrigerator.service.RefrigeratorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/refrigerator")
@RequiredArgsConstructor
public class RefrigeratorController {
    private final RefrigeratorService refrigeratorService;

    @PostMapping
    public SuccessResponse<RefrigeratorInfoResponse> createRefrigerator(
            @AuthenticationPrincipal Long userId,
            @RequestBody RefrigeratorNameRequest request) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_CREATE_SUCCESS,
                refrigeratorService.createRefrigerator(new CreateRefrigeratorRequest(userId, request.name())));
    }

    @DeleteMapping("/{refrigeratorId}")
    public SuccessResponse<String> deleteRefrigerator(
            @PathVariable Long refrigeratorId) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_DELETE_SUCCESS,
                refrigeratorService.deleteRefrigerator(new DeleteRefrigeratorRequest(refrigeratorId)));
    }

    @PutMapping("/{refrigeratorId}")
    public SuccessResponse<RefrigeratorInfoResponse> updateRefrigerator(
            @NotNull @PathVariable Long refrigeratorId,
            @Valid @RequestBody RefrigeratorNameRequest request) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_UPDATE_SUCCESS,
                refrigeratorService.updateRefrigerator(new UpdateRefrigeratorRequest(refrigeratorId, request.name())));
    }

    @GetMapping
    public SuccessResponse<List<RefrigeratorInfoResponse>> getAllRefrigerator(
            @AuthenticationPrincipal Long userId
    ) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_LIST_SUCCESS,
                refrigeratorService.getAllRefrigerator(new GetAllRefrigeratorRequest(userId)));
    }

    @PostMapping("/{refrigeratorId}/user")
    public SuccessResponse<List<RefrigeratorInfoResponse>> addUserToRefrigerator(
            @AuthenticationPrincipal Long userId,
            @NotNull @PathVariable Long refrigeratorId
    ) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_LIST_SUCCESS,
                refrigeratorService.addUserToRefrigerator(new RefrigeratorUserRequest(userId, refrigeratorId)));
    }

    @DeleteMapping("/{refrigeratorId}/user")
    public SuccessResponse<List<RefrigeratorInfoResponse>> deleteUserToRefrigerator(
            @AuthenticationPrincipal Long userId,
            @NotNull @PathVariable Long refrigeratorId
    ) {
        return SuccessResponse.of(RefrigeratorSuccessCode.REFRIGERATOR_LIST_SUCCESS,
                refrigeratorService.deleteUserToRefrigerator(new RefrigeratorUserRequest(userId, refrigeratorId)));
    }
}
