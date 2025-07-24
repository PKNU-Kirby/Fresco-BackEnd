package com.example.fresco.grocerylist.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.GrocerySuccessCode;
import com.example.fresco.grocerylist.dto.request.GroceryItemDtoRequest;
import com.example.fresco.grocerylist.dto.request.GroceryItemUpdateDtoRequest;
import com.example.fresco.grocerylist.dto.response.GroceryListDtoResponse;
import com.example.fresco.grocerylist.service.GroceryService;
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

    @PostMapping("/item")
    public SuccessResponse<GroceryItemDtoRequest> addItem(@RequestBody GroceryItemDtoRequest dto) {
        GroceryItemDtoRequest saved = groceryService.addItem(dto);
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_ADD_SUCCESS, saved);
    }

    @GetMapping("/{groceryListId}")
    public SuccessResponse<GroceryListDtoResponse> getList(@PathVariable("groceryListId") Long id) {
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_LIST_SUCCESS,
                groceryService.getListWithItems(id)
        );
    }

    @PatchMapping("/{groceryListId}/update")
    public SuccessResponse<GroceryListDtoResponse> updateItems(
            @PathVariable Long groceryListId,
            @RequestBody List<GroceryItemUpdateDtoRequest> dtos
    ) {
        GroceryListDtoResponse updatedList = groceryService.updateItems(groceryListId, dtos);
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_UPDATE_SUCCESS, updatedList);
    }

    @DeleteMapping("/{groceryListId}/delete")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable("groceryListId") Long id) {

        groceryService.deleteItem(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");

        return ResponseEntity.ok(response);

    }
}

