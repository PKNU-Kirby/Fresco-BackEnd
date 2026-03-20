package com.example.fresco.recipe.controller.dto.response;

import java.util.List;

public record OpenAiRecipeDto(
        String title,
        List<Item> ingredients,
        List<String> steps,
        List<Substitution> substitutions
) {
    public record Item(String ingredientName, Double quantity, String unit) {
    }

    public record Substitution(String original, String substitute) {
    }
}
