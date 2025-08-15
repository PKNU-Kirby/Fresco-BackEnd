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
import com.example.fresco.ingredient.controller.dto.request.SaveIngredientsRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchListResponse;
import com.example.fresco.ingredient.controller.dto.response.ocr.FoodPair;
import com.example.fresco.ingredient.controller.dto.response.ocr.ReceiptResponse;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.ingredient.service.util.ocr.ImageUtils;
import com.example.fresco.ingredient.service.util.ocr.NaverOcrClient;
import com.example.fresco.ingredient.service.util.ocr.ReceiptApiClient;
import com.example.fresco.ingredient.service.util.ocr.ReceiptOcrResponseParser;
import com.example.fresco.ingredient.service.util.update.UpdateIngredientConditionManager;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;
    private final NaverOcrClient naverOcrClient;
    private final ReceiptOcrResponseParser receiptOcrResponseParser;
    private final ReceiptApiClient receiptClient;

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
    public List<IngredientResponse> saveIngredient(Long refrigeratorId, SaveIngredientsRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId).orElseThrow(
                () -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));

        List<Long> ingredientIds = request.getIngredientIds();
        List<Ingredient> allIngredients = ingredientRepository.findAllById(ingredientIds);

        Map<Long, Ingredient> ingredientMap = allIngredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, Function.identity()));

        List<RefrigeratorIngredient> refrigeratorIngredients = request.ingredientsInfo().stream()
                .map(info -> {
                    Ingredient ingredient = ingredientMap.get(info.ingredientId());
                    return new RefrigeratorIngredient(
                            refrigerator,
                            ingredient,
                            ingredient.getCategory(),
                            info.expirationDate()
                    );
                })
                .toList();

        List<RefrigeratorIngredient> savedIngredients = refrigeratorIngredientRepository.saveAll(refrigeratorIngredients);
        return IngredientResponse.getListFromRefrigeratorIngredients(savedIngredients);
    }

    @Transactional
    public ReceiptMatchListResponse registerFromReceipt(MultipartFile receiptImage) {
        ImageUtils.validateImageFile(receiptImage);
        String base64Image = ImageUtils.encodeToBase64(receiptImage);

        ReceiptResponse receiptResponse = naverOcrClient.callOcrApi(base64Image);
        List<FoodPair> foodPairs = receiptOcrResponseParser.parseReceiptResponse(receiptResponse);
        List<String> foodNames = foodPairs.stream().map(FoodPair::food).toList();

        ReceiptMatchListResponse receiptMatchList = receiptClient.sendReceipt(foodNames);
        List<Long> ingredientIds = receiptMatchList.getIngredientIds();

        List<Ingredient> allIngredients = ingredientRepository.findAllById(ingredientIds);

        Map<Long, LocalDate> expirationDateMap = allIngredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        ingredient -> LocalDate.now().plusDays(ingredient.getDefaultUseByPeriod())
                ));

        return;
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