package com.example.fresco.ingredient.controller.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        PageInfo pageInfo
) {
}