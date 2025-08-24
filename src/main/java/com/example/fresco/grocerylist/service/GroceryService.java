package com.example.fresco.grocerylist.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.GroceryListErrorCode;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemDeleteDtoRequest;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemDtoRequest;
import com.example.fresco.grocerylist.controller.dto.request.GroceryItemUpdateDtoRequest;
import com.example.fresco.grocerylist.controller.dto.response.GroceryListDtoResponse;
import com.example.fresco.grocerylist.domain.GroceryItem;
import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.grocerylist.domain.repository.GroceryItemRepository;
import com.example.fresco.grocerylist.domain.repository.GroceryListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroceryService {
    private final GroceryListRepository groceryListRepository;
    private final GroceryItemRepository groceryItemRepository;

    @Transactional
    public GroceryItemDtoRequest addItem(GroceryItemDtoRequest dto) {
        GroceryList list = groceryListRepository.findById(dto.groceryListId())
                .orElseThrow(() -> new RestApiException(GroceryListErrorCode.NULL_GROCERYLIST));

        GroceryItem item = GroceryItem.builder()
                .name(dto.name())
                .purchased(dto.purchased())
                .quantity(dto.quantity())
                .groceryList(list)
                .build();

        GroceryItem saved = groceryItemRepository.save(item);
        return GroceryItemDtoRequest.from(saved);
    }

    @Transactional(readOnly = true)
    public GroceryListDtoResponse getListWithItems(Long groceryListId) {
        List<GroceryItem> itemList = groceryItemRepository.findAllByGroceryListId(groceryListId);
        return GroceryListDtoResponse.from(groceryListId, itemList);
    }

    @Transactional
    public GroceryListDtoResponse updateItems(Long groceryListId, List<GroceryItemUpdateDtoRequest> dtos) {
        for (GroceryItemUpdateDtoRequest dto : dtos) {
            GroceryItem item = groceryItemRepository.findById(dto.id())
                    .orElseThrow(() -> new RestApiException(GroceryListErrorCode.ITEM_NOT_FOUND));

            if (dto.name() != null) item.updateName(dto.name());
            if (dto.quantity() != null) item.updateQuantity(dto.quantity());
            if (dto.purchased() != null) item.updatePurchased(dto.purchased());
        }

        List<GroceryItem> itemList = groceryItemRepository.findAllByGroceryListId(groceryListId);

        return GroceryListDtoResponse.from(groceryListId, itemList);
    }

    @Transactional
    public GroceryListDtoResponse deleteItems(Long groceryListId, GroceryItemDeleteDtoRequest itemIds) {
        groceryItemRepository.deleteAllById(itemIds.itemIds());

        List<GroceryItem> itemList = groceryItemRepository.findAllByGroceryListId(groceryListId);
        return GroceryListDtoResponse.from(groceryListId, itemList);
    }
}
