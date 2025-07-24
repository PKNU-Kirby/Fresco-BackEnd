package com.example.fresco.grocerylist.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.GrocerySuccessCode;
import com.example.fresco.grocerylist.domain.GroceryItem;
import com.example.fresco.grocerylist.dto.*;
import com.example.fresco.grocerylist.service.GroceryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/grocery")
@RequiredArgsConstructor
public class GroceryController {

    private final GroceryService groceryService;

    @PostMapping("/item")
    public SuccessResponse<GroceryItemDtoRequest> addItem(@RequestBody GroceryItemDtoRequest dto) {
        GroceryItemDtoRequest saved = groceryService.addItem(dto);
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_ADD_SUCCESS, saved);
    }

    @GetMapping("/{id}")
    public SuccessResponse<GroceryListDtoResponse> getList(@PathVariable("id") Long id) {
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_LIST_SUCCESS,
                groceryService.getListWithItems(id)
        );
    }

//    @PatchMapping("/{id}/purchased")
//    public GroceryItem togglePurchased(@PathVariable Long id, @RequestBody PurchasedUpdateDto dto) {
//        return groceryService.updatePurchased(id,dto.purchased());
//    }
//
//    @PatchMapping("/{id}/name")
//    public GroceryItem updateName(@PathVariable Long id, @RequestBody NameUpdateDto dto) {
//        return groceryService.updateName(id, dto.name());
//    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long id) {

        groceryService.deleteItem(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");

        return ResponseEntity.ok(response);

    }
}

