package fresco.com.ingredient.util;

import fresco.com.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import fresco.com.ingredient.domain.Ingredient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
class UpdateIngredientExpirationDateHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final LocalDate expirationDate = updateContractConditionCommand.expirationDate();
        if (Objects.nonNull(expirationDate)) {
            ingredient.updateExpirationDate(expirationDate);
        }
    }
}
