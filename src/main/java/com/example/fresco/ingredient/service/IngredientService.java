package com.example.fresco.ingredient.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.global.response.error.RefrigeratorIngredientErrorCode;
import com.example.fresco.global.response.error.UserErrorCode;
import com.example.fresco.history.domain.History;
import com.example.fresco.history.domain.repository.HistoryRepository;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.global.response.paging.PageInfo;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.ingredient.service.util.UpdateIngredientConditionManager;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;

    @Transactional(readOnly = true)
    public PageResponse<IngredientResponse> getIngredients(Long refrigeratorId, IngredientFilterRequest filter) {
        Sort sortType = getSortType(filter);
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sortType);
        Page<IngredientResponse> page = refrigeratorIngredientRepository.findByRefrigeratorIdAndCategoryIdIn(refrigeratorId, filter.getCategoryIds(), pageRequest);
        return new PageResponse<>(page.getContent(), PageInfo.getPageInfo(page));
    }

    private Sort getSortType(IngredientFilterRequest filter) {
        if (filter.getSort().equals("expirationDateDesc"))
            return Sort.by(Sort.Direction.DESC, "expirationDate");
        else
            return Sort.by(Sort.Direction.ASC, "expirationDate");
    }

    @Transactional
    public IngredientResponse updateIngredient(UpdateIngredientConditionCommand updateIngredientConditionCommand) {
        RefrigeratorIngredient refrigeratorIngredient = refrigeratorIngredientRepository.findById(updateIngredientConditionCommand.refrigeratorIngredientId())
                .orElseThrow(() -> new RestApiException(IngredientErrorCode.NULL_INGREDIENT));

        updateIngredientConditionManager.updateContract(refrigeratorIngredient, updateIngredientConditionCommand);
        saveUsedHistory(updateIngredientConditionCommand);
        RefrigeratorIngredient savedIngredient = refrigeratorIngredientRepository.save(refrigeratorIngredient);
        return IngredientResponse.from(savedIngredient);
    }

    private void saveUsedHistory(UpdateIngredientConditionCommand command) {
        RefrigeratorIngredient prevIngredient = refrigeratorIngredientRepository.findById(command.refrigeratorIngredientId())
                .orElseThrow(() -> new RestApiException(RefrigeratorIngredientErrorCode.NULL_REFRIGERATOR_INGREDIENT));
        User consumer = userRepository.findById(command.userId()).orElseThrow(() -> new RestApiException(UserErrorCode.NULL_USER));
        double usedQuantity = prevIngredient.getQuantity() - command.quantity();

        historyRepository.save(new History(consumer, prevIngredient, usedQuantity));
    }
}