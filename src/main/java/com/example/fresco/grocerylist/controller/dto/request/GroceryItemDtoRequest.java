package com.example.fresco.grocerylist.controller.dto.request;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryItemDtoRequest(
        Long id,
        String name,
        Integer quantity,
        String unit,
        Boolean purchased,
        Long groceryListId
) {
    public static GroceryItemDtoRequest from(GroceryItem item) {
        return new GroceryItemDtoRequest(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getPurchased(),
                item.getGroceryList().getId()
        );
    }
}
