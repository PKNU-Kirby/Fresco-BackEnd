package com.example.fresco.ingredient.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientMatchInfo {
    @JsonProperty("input_name")
    String inputName;
    @JsonProperty("product_name")
    String productName;
    @JsonProperty("category_name")
    String categoryName;
    Integer value;
}