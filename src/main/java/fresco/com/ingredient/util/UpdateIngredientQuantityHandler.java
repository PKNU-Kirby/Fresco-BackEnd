package fresco.com.ingredient.util;

import fresco.com.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import fresco.com.ingredient.domain.Ingredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientQuantityHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final Integer quantity = updateContractConditionCommand.quantity();
        if (Objects.nonNull(quantity)) {
            ingredient.updateQuantity(quantity);
        }
    }
}