package fresco.com.grocerylist.controller;

import fresco.com.grocerylist.domain.Grocery;
import fresco.com.grocerylist.dto.GroceryDto;
import fresco.com.grocerylist.service.GroceryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grocery")
@RequiredArgsConstructor
public class GroceryController {

    private final GroceryService groceryService;

    @PostMapping
    public Grocery addItem(@RequestBody GroceryDto dto) {
        return groceryService.addItem(dto.getName());
    }

    @GetMapping
    public List<Grocery> getItems() {
        return groceryService.getAllItems();
    }

    @PatchMapping("/{id}/purchased")
    public Grocery togglePurchased(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean purchased = body.getOrDefault("purchased", false);
        return groceryService.updatePurchased(id,purchased);
    }

    @PatchMapping("/{id}/name")
    public Grocery updateName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        return groceryService.updateName(id, name);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        groceryService.deleteItem(id);
    }
}

