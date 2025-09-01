package com.example.fresco.ingredient.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientFilterRequest {
    private List<Short> categoryIds;
    private String sort;
    private Integer page;
    private Integer size;
}