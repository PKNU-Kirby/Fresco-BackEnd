package fresco.com.grocerylist.controller;

import fresco.com.grocerylist.domain.Grocery;
import fresco.com.grocerylist.dto.GroceryDto;
import fresco.com.grocerylist.dto.NameUpdateDto;
import fresco.com.grocerylist.dto.PurchasedUpdateDto;
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
        return groceryService.addItem(dto.name());
    }

    @GetMapping
    public List<Grocery> getItems() {
        return groceryService.getAllItems();
    }

    @PatchMapping("/{id}/purchased")
    public Grocery togglePurchased(@PathVariable Long id, @RequestBody PurchasedUpdateDto dto) {
        return groceryService.updatePurchased(id,dto.purchased());
    }

    @PatchMapping("/{id}/name")
    public Grocery updateName(@PathVariable Long id, @RequestBody NameUpdateDto dto) {
        return groceryService.updateName(id, dto.name());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long id) {

        groceryService.deleteItem(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");

        return ResponseEntity.ok(response);

    }
}

