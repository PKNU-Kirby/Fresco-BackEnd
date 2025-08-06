package com.example.fresco.recipe.controller.dto.response;

import java.util.List;

public record OpenAiResponse (
        String title,
        List<Item> ingredients,
        List<String> steps,
        List<Substitution> substitutions
){
    public record Item(String ingredient, double quantity, String scale) {}
    public record Substitution (String original, String substitute) {}
}
