package com.example.fresco.grocerylist.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.GrocerySuccessCode;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemDeleteDtoRequest;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemDtoRequest;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemUpdateDtoRequest;
import com.example.fresco.grocerylist.controller.dto.response.GroceryListDtoResponse;
import com.example.fresco.grocerylist.service.GroceryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody List<GroceryItemUpdateDtoRequest> items
    ) {
        GroceryListDtoResponse updatedList = groceryService.updateItems(groceryListId, items);
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_UPDATE_SUCCESS, updatedList);
    }

    @DeleteMapping("/{groceryListId}/delete")
    public SuccessResponse<GroceryListDtoResponse> deleteItems(
            @PathVariable Long groceryListId,
            @RequestBody GroceryItemDeleteDtoRequest request
    ) {
        GroceryListDtoResponse updatedList = groceryService.deleteItems(groceryListId, request);
        return SuccessResponse.of(GrocerySuccessCode.GROCERY_DELETE_SUCCESS, updatedList);
    }
}

