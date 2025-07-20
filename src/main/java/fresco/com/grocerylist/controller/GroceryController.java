package fresco.com.grocerylist.controller;

import fresco.com.grocerylist.domain.Grocery;
import fresco.com.grocerylist.dto.GroceryDto;
import fresco.com.grocerylist.service.GroceryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        Boolean purchased = body.get("purchased");
        return groceryService.updatePurchased(id,purchased);
    }

    @PatchMapping("/{id}/name")
    public Grocery updateName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        return groceryService.updateName(id, name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long id) {

        groceryService.deleteItem(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");

        return ResponseEntity.ok(response);

    }
}

