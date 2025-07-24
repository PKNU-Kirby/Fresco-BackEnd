package com.example.fresco.grocerylist.service;

import com.example.fresco.grocerylist.domain.GroceryItem;
import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.grocerylist.dto.request.GroceryItemDeleteDtoRequest;
import com.example.fresco.grocerylist.dto.request.GroceryItemDtoRequest;
import com.example.fresco.grocerylist.dto.request.GroceryItemUpdateDtoRequest;
import com.example.fresco.grocerylist.dto.response.GroceryListDtoResponse;
import com.example.fresco.grocerylist.repository.GroceryListRepository;
import com.example.fresco.grocerylist.repository.GroceryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroceryService {
    private final GroceryListRepository groceryListRepository;
    private final GroceryRepository groceryRepository;

    public GroceryItemDtoRequest addItem(GroceryItemDtoRequest dto) {
        GroceryList list = groceryListRepository.findById(dto.groceryListId())
                .orElseThrow(() -> new RuntimeException("해당 장보기 리스트가 존재하지 않습니다."));

        GroceryItem item = GroceryItem.builder()
                .name(dto.name())
                .purchased(dto.purchased())
                .quantity(dto.quantity())
                .groceryList(list)
                .build();

        GroceryItem saved = groceryRepository.save(item);
        return GroceryItemDtoRequest.from(saved);
    }

    public GroceryListDtoResponse getListWithItems(Long listId) {
        GroceryList list = groceryListRepository.findWithItemsById(listId)
                .orElseThrow(() -> new RuntimeException("리스트를 찾을 수 없습니다."));
        return GroceryListDtoResponse.from(list.getId(), list.getItems());
    }

    public GroceryListDtoResponse updateItems(Long listId, List<GroceryItemUpdateDtoRequest> dtos) {
        for (GroceryItemUpdateDtoRequest dto : dtos) {
            GroceryItem item = groceryRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found: " + dto.id()));

            if (dto.name() != null) item.updateName(dto.name());
            if (dto.quantity() != null) item.updateQuantity(dto.quantity());
            if (dto.purchased() != null) item.updatePurchased(dto.purchased());
        }

        GroceryList list = groceryListRepository.findWithItemsById(listId)
                .orElseThrow(() -> new RuntimeException("리스트를 찾을 수 없습니다."));

        return GroceryListDtoResponse.from(list.getId(), list.getItems());
    }

    public GroceryListDtoResponse deleteItems(Long groceryListId, GroceryItemDeleteDtoRequest itemIds) {
        for (Long itemId : itemIds.itemIds()) {
            groceryRepository.deleteById(itemId);
        }
        GroceryList list = groceryListRepository.findWithItemsById(groceryListId)
                .orElseThrow(() -> new RuntimeException("리스트를 찾을 수 없습니다."));

        return GroceryListDtoResponse.from(list.getId(), list.getItems());
    }
}
