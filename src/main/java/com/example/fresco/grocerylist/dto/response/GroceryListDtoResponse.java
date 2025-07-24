package com.example.fresco.grocerylist.dto.response;


import com.example.fresco.grocerylist.domain.GroceryItem;

import java.util.List;

public record GroceryListDtoResponse(Long  groceryListId, List<GroceryItemDtoResponse> items) {
    public static GroceryListDtoResponse from(Long listId, List<GroceryItem> items) {
        List<GroceryItemDtoResponse> result = items.stream()
                .map(GroceryItemDtoResponse::itemDto)
                .toList();
        return new GroceryListDtoResponse(listId, result);
    }
}
