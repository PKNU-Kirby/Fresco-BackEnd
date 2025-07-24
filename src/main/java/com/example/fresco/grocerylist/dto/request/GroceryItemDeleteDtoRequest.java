package com.example.fresco.grocerylist.dto.request;

import java.util.List;

public record GroceryItemDeleteDtoRequest(
        List<Long> itemIds
) {}
