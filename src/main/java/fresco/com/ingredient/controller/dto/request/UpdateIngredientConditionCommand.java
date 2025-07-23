package fresco.com.ingredient.controller.dto.request;

import lombok.NonNull;

import java.time.LocalDate;

public record UpdateIngredientConditionCommand(
        @NonNull
        Long id,
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
}
