package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.domain.Ingredient;
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
