package com.example.fresco.grocerylist.dto.request;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryItemDtoRequest(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased,
        Long groceryListId
) {
    public static GroceryItemDtoRequest from(GroceryItem item) {
        return new GroceryItemDtoRequest(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getPurchased(),
                item.getGroceryList().getId()
        );
    }
}
