package com.example.fresco.recipe.controller.dto;

import java.util.List;

public record OpenAiResponse (
        String title,
        List<String> ingredients,
        List<String> steps,
        List<Substitution> substitutions
){
    public record Substitution (String original, String substitute) {}
}
