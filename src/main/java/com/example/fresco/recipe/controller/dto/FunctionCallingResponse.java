package com.example.fresco.recipe.controller.dto;

import java.util.List;

public record FunctionCallingResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {}

    public record Message(
            FunctionCall function_call
    ) {}

    public record FunctionCall(
            String name,
            String arguments
    ) {}
}
