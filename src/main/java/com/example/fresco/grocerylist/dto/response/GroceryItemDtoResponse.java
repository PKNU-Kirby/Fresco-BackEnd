package com.example.fresco.grocerylist.dto.response;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryItemDtoResponse(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased
){
    static public GroceryItemDtoResponse itemDto(GroceryItem item) {
        return new GroceryItemDtoResponse(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getPurchased()
        );
    }
}
