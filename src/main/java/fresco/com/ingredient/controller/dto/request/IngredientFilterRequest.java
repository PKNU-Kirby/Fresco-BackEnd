package fresco.com.ingredient.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientFilterRequest {
    private List<Integer> categoryIds;
    private String sort;
    private Integer page;
    private Integer size;
}