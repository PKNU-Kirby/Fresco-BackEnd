package fresco.com.grocerylist.dto;

import fresco.com.grocerylist.domain.GroceryItem;

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
