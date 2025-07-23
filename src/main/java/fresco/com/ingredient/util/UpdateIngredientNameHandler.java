package fresco.com.ingredient.util;

import fresco.com.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import fresco.com.ingredient.domain.Ingredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientNameHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final String name = updateContractConditionCommand.name();
        if (Objects.nonNull(name)) {
            ingredient.updateName(name);
        }
    }
}
