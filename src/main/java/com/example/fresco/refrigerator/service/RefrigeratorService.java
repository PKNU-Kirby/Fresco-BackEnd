package com.example.fresco.refrigerator.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.example.fresco.global.response.error.RefrigeratorErrorCode;
import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.grocerylist.domain.repository.GroceryListRepository;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.CreateRefrigeratorRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.DeleteRefrigeratorRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.GetAllRefrigeratorRequest;
import com.example.fresco.refrigerator.controller.dto.request.refrigerator.UpdateRefrigeratorRequest;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorEditableResponse;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorUser;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorInvitationRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorUserRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class RefrigeratorService {
    private final UserRepository userRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final GroceryListRepository groceryListRepository;

    @Transactional
    public RefrigeratorInfoResponse createRefrigerator(@Valid CreateRefrigeratorRequest request) {
        Refrigerator savedRefrigerator = refrigeratorRepository.save(new Refrigerator(request.name(), request.userId()));
        GroceryList savedGroceryList = saveGroceryList(savedRefrigerator);
        log.info("userId : {}", request.userId());
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        refrigeratorUserRepository.save(new RefrigeratorUser(savedRefrigerator, user));
        return new RefrigeratorInfoResponse(savedRefrigerator.getId(), savedRefrigerator.getName(), savedGroceryList.getId());
    }

    @Transactional
    public String deleteRefrigerator(@Valid DeleteRefrigeratorRequest request) {
        refrigeratorRepository.deleteById(request.refrigeratorId());
        refrigeratorUserRepository.deleteByRefrigeratorId(request.refrigeratorId());
        return "성공적으로 삭제되었습니다.";
    }

    @Transactional
    public RefrigeratorInfoResponse updateRefrigerator(@Valid UpdateRefrigeratorRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(request.refrigeratorId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));
        GroceryList groceryList = groceryListRepository.findByRefrigerator(refrigerator);
        refrigerator.changeName(request.name());

        return new RefrigeratorInfoResponse(refrigerator.getId(), refrigerator.getName(), groceryList.getId());
    }

    @Transactional(readOnly = true)
    public List<RefrigeratorInfoResponse> getAllRefrigerator(GetAllRefrigeratorRequest request) {
        return refrigeratorUserRepository.findAllRefrigeratorsByUserId(request.userId());
    }

    /**
     * dirty checking이 아닌 saved를 호출한 이유
     * <p>
     * 이 메서드를 사용할 때는 냉장고를 처음 생성할 때이므로, 더티 체킹을 할 수 없다.
     * 그러므로, 명시적으로 save를 사용하여 저장하여야 한다.
     */
    private GroceryList saveGroceryList(Refrigerator refrigerator) {
        GroceryList groceryList = GroceryList.builder()
                .refrigerator(refrigerator)
                .totalAmount(0)
                .build();
        return groceryListRepository.save(groceryList);
    }

    @Transactional(readOnly = true)
    public Map<Long, Boolean> getEditableMapByUser(Long userId) {
        List<Long> refrigeratorIds = refrigeratorUserRepository.findRefrigeratorIdsByUserId(userId);

        if (refrigeratorIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<RefrigeratorEditableResponse> editableRows =
                refrigeratorRepository.findEditableRowsByIds(refrigeratorIds, userId);

        Map<Long, Boolean> result = new LinkedHashMap<>();
        for (RefrigeratorEditableResponse row : editableRows) {
            result.put(row.refrigeratorId(), row.editable());
        }
        return result;
    }
}