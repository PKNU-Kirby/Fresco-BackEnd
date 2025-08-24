package com.example.fresco.grocerylist.controller.dto.response;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryItemDtoResponse(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased
) {
    public static GroceryItemDtoResponse itemDto(GroceryItem item) {
        return new GroceryItemDtoResponse(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getPurchased()
        );
    }
}
