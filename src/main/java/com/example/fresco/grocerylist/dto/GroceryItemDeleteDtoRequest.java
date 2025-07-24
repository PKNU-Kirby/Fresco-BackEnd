package com.example.fresco.grocerylist.dto;

import java.util.List;

public record GroceryItemDeleteDtoRequest(
        List<GroceryItemUpdateDtoRequest> updateItems
) {}
