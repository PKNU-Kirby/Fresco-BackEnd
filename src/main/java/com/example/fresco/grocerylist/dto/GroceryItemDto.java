package com.example.fresco.grocerylist.dto;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryItemDto(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased
){
    static public GroceryItemDto itemDto(GroceryItem item) {
        return new GroceryItemDto(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getPurchased()
        );
    }
}
