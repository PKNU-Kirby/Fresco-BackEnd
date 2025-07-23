package fresco.com.ingredient.controller.dto.response;

import fresco.com.ingredient.domain.Ingredient;

import java.time.LocalDate;
import java.util.Date;

public record IngredientResponse(
        Long id,
        Long categoryId,
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
    public static IngredientResponse from(Ingredient ingredient){
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getCategory().getId(),
                ingredient.getName(),
                ingredient.getExpirationDate(),
                ingredient.getQuantity()
        );
    }
}
