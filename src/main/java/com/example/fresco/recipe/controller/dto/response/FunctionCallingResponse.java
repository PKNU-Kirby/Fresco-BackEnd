package com.example.fresco.recipe.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FunctionCallingResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {
        public record Message(
                String role,
                @JsonProperty("tool_calls") List<ToolCall> toolCalls
        ) {
            public record ToolCall(
                    Function function
            ) {
                public record Function(
                        String name,
                        String arguments
                ) {}
            }
        }
    }
}
