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
import com.example.fresco.ingredient.controller.dto.response.IngredientListResponse;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchListResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptOcrMappingResponse;
import com.example.fresco.ingredient.controller.dto.response.ocr.ReceiptResponse;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.ingredient.service.util.dataClient.DataApiClient;
import com.example.fresco.ingredient.service.util.dataClient.ReceiptOcrResponseParser;
import com.example.fresco.ingredient.service.util.ocr.ImageUtils;
import com.example.fresco.ingredient.service.util.ocr.NaverOcrClient;
import com.example.fresco.ingredient.service.util.update.UpdateIngredientConditionManager;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class IngredientService {
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;
    private final NaverOcrClient naverOcrClient;
    private final ReceiptOcrResponseParser receiptOcrResponseParser;
    private final DataApiClient dataApiClient;

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
    public List<ReceiptOcrMappingResponse> registerFromReceipt(MultipartFile receiptImage) {
        ImageUtils.validateImageFile(receiptImage);
        String base64Image = ImageUtils.encodeToBase64(receiptImage);

        // ocr 결과
        ReceiptResponse receiptResponse = naverOcrClient.callOcrApi(base64Image);
        List<String> productNames = receiptOcrResponseParser.parseReceiptResponse(receiptResponse);
        long ocrEndTime = System.currentTimeMillis();
        log.info("ocr 종료 시간 : {}", ocrEndTime);

        // elasticsearch 결과
        ReceiptMatchListResponse receiptMatchList = dataApiClient.sendReceipt(productNames);
        long receiptEndTime = System.currentTimeMillis();
        log.info("영수증 종료 시간 : {}", receiptEndTime);

        List<Long> ingredientIds = receiptMatchList.getIngredientIds();

        List<Ingredient> allIngredients = ingredientRepository.findAllById(ingredientIds);

        Map<Long, LocalDate> expirationDateMap = allIngredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        ingredient -> LocalDate.now().plusDays(ingredient.getDefaultUseByPeriod())
                ));


        return receiptMatchList.receiptMatchList().stream()
                .map(matchInfo ->
                        ReceiptOcrMappingResponse.from(matchInfo, expirationDateMap.get(matchInfo.ingredientId())))
                .toList();
    }

    @Transactional
    public List<IngredientResponse> registerFromPhoto(MultipartFile ingredientImage) {
        // 사진 보내기
        IngredientListResponse ingredientImageListResponse = dataApiClient.sendImage(ingredientImage);

        // 응답 변환
        List<Long> ingredientIds = ingredientImageListResponse.getIngredientIds();
        List<Ingredient> allIngredients = ingredientRepository.findAllById(ingredientIds);

        Map<Long, LocalDate> expirationDateMap = allIngredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        ingredient -> LocalDate.now().plusDays(ingredient.getDefaultUseByPeriod())
                ));

        return IngredientListResponse.getIngredientResponseWithExpirationDate(ingredientImageListResponse.imageList(), expirationDateMap);
    }

    private void saveUsedHistory(UpdateIngredientConditionCommand command) {
        RefrigeratorIngredient prevIngredient = refrigeratorIngredientRepository.findById(command.refrigeratorIngredientId())
                .orElseThrow(() -> new RestApiException(RefrigeratorIngredientErrorCode.NULL_REFRIGERATOR_INGREDIENT));
        User consumer = userRepository.findById(command.userId()).orElseThrow(() -> new RestApiException(UserErrorCode.NULL_USER));

        historyRepository.save(new History(consumer, prevIngredient));
    }
}