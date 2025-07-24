package com.example.fresco.grocerylist.dto;


public record GroceryItemUpdateDtoRequest(
        Long id,
        String name,
        Integer quantity,
        Boolean purchased
) {}
