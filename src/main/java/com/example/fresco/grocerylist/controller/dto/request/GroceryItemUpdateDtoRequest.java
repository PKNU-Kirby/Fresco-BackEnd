package com.example.fresco.grocerylist.controller.dto.request;


public record GroceryItemUpdateDtoRequest(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased
) {
}
