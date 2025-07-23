package com.example.fresco.grocerylist.dto;

import com.example.fresco.grocerylist.domain.GroceryItem;

public record GroceryDto (
        Long id,
        String name,
        Boolean purchased
){
    static public GroceryDto toDto(GroceryItem groceryItem) {
        return new GroceryDto(
                groceryItem.getId(),
                groceryItem.getName(),
                groceryItem.getPurchased()
        );
    }
}
