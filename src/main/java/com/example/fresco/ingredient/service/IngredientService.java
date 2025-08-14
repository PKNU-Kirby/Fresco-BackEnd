package com.example.fresco.ingredient.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.global.response.error.RefrigeratorErrorCode;
import com.example.fresco.global.response.error.RefrigeratorIngredientErrorCode;
import com.example.fresco.global.response.error.UserErrorCode;
import com.example.fresco.global.response.paging.PageInfo;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.history.domain.History;
import com.example.fresco.history.domain.repository.HistoryRepository;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.controller.dto.request.CreateIngredientsRequest;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchResponse;
import com.example.fresco.ingredient.domain.CategoryCache;
import com.example.fresco.ingredient.service.util.ocr.ReceiptApiClient;
import com.example.fresco.ingredient.service.util.update.UpdateIngredientConditionManager;
import com.example.fresco.ingredient.service.util.ocr.ImageUtils;
import com.example.fresco.ingredient.service.util.ocr.NaverOcrClient;
import com.example.fresco.ingredient.service.util.ocr.ReceiptOcrResponseParser;
import com.example.fresco.ingredient.controller.dto.response.ocr.FoodPair;
import com.example.fresco.ingredient.controller.dto.response.ocr.ReceiptResponse;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;
    private final NaverOcrClient naverOcrClient;
    private final ReceiptOcrResponseParser receiptOcrResponseParser;
    private final ReceiptApiClient receiptClient;
    private final CategoryCache categoryCache;

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

    @Transactional
    public IngredientResponse createIngredient(Long refrigeratorId, CreateIngredientsRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId).orElseThrow(
                () -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));
        List<String> foodNames = request.ingredientsInfo().stream().map(CreateIngredientsRequest.CreateIngredientInfo::name).toList();
        ReceiptMatchResponse receiptMatchResponse = receiptClient.sendReceipt(foodNames);

        // 카테고리 id 구하기
        Map<String, Long> categoryIds = categoryCache.getCategoryIds(receiptMatchResponse.getCategoryNames());
        // 식재료 id 구하기
        RefrigeratorIngredient.from(refrigerator, request.ingredientsInfo());
    }

    @Transactional
    public ReceiptMatchResponse registerFromReceipt(MultipartFile receiptImage) {
        ImageUtils.validateImageFile(receiptImage);
        String base64Image = ImageUtils.encodeToBase64(receiptImage);

        ReceiptResponse receiptResponse = naverOcrClient.callOcrApi(base64Image);
        List<FoodPair> foodPairs = receiptOcrResponseParser.parseReceiptResponse(receiptResponse);
        List<String> foodNames = foodPairs.stream().map(FoodPair::food).toList();

        ReceiptMatchResponse receiptMatchResponse = receiptClient.sendReceipt(foodNames);
        return receiptMatchResponse.defineQuantity(foodPairs);
    }

//    @Transactional
//    public List<IngredientResponse> registerFromPhoto(Long refrigeratorId, MultipartFile ingredientImage) {
//    }

    private void saveUsedHistory(UpdateIngredientConditionCommand command) {
        RefrigeratorIngredient prevIngredient = refrigeratorIngredientRepository.findById(command.refrigeratorIngredientId())
                .orElseThrow(() -> new RestApiException(RefrigeratorIngredientErrorCode.NULL_REFRIGERATOR_INGREDIENT));
        User consumer = userRepository.findById(command.userId()).orElseThrow(() -> new RestApiException(UserErrorCode.NULL_USER));
        int usedQuantity = prevIngredient.getQuantity() - command.quantity();

        historyRepository.save(new History(consumer, prevIngredient, usedQuantity));
    }
}