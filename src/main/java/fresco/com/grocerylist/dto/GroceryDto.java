package fresco.com.grocerylist.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GroceryDto {
    private Long id;
    private String name;
    private boolean purchased;
}
