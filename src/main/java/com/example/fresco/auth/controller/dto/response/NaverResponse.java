package com.example.fresco.auth.controller.dto.response;

import com.example.fresco.auth.domain.Provider;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NaverResponse(
        @NotNull(message = "null이면 안 됩니다.")
        Map<String, String> attributes) implements SocialResponse {

    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id");
    }

    @Override
    public String getName() {
        return attributes.get("ingredientName");
    }
}
