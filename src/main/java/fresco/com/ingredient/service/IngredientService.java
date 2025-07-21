package fresco.com.ingredient.service;

import fresco.com.ingredient.controller.dto.request.IngredientFilterRequest;
import fresco.com.ingredient.controller.dto.response.IngredientResponse;
import fresco.com.ingredient.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredients(Long refrigeratorId, IngredientFilterRequest filter) {
        Sort sortType = getSortType(filter);
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sortType);
        return ingredientRepository.findByRefrigeratorIdAndCategoryIdIn(refrigeratorId, filter.getCategoryIds(), pageRequest)
                .stream()
                .map(ingredient -> IngredientResponse.from(ingredient))
                .collect(Collectors.toList());
    }

    private Sort getSortType(IngredientFilterRequest filter) {
        if (filter.getSort().equals("expirationDateDesc"))
            return Sort.by(Sort.Direction.DESC, "expirationDate");
        else
            return Sort.by(Sort.Direction.ASC, "expirationDate");
    }
}
