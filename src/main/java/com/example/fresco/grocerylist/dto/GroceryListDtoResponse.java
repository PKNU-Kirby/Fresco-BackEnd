package com.example.fresco.grocerylist.dto;


import com.example.fresco.grocerylist.domain.GroceryItem;

import java.util.List;

public record GroceryListDtoResponse(Long  groceryListId, List<GroceryItemDto> items) {
    public static GroceryListDtoResponse from(Long listId, List<GroceryItem> items) {
        List<GroceryItemDto> result = items.stream()
                .map(GroceryItemDto::itemDto)
                .toList();
        return new GroceryListDtoResponse(listId, result);
    }
}
