package com.example.fresco.grocerylist.controller.dto.request;

import java.util.List;

public record GroceryItemDeleteDtoRequest(
        List<Long> itemIds
) {}
