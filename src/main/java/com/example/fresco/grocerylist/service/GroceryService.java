package com.example.fresco.grocerylist.service;

import com.example.fresco.grocerylist.domain.GroceryItem;
import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.grocerylist.dto.GroceryItemDtoRequest;
import com.example.fresco.grocerylist.dto.GroceryListDtoResponse;
import com.example.fresco.grocerylist.repository.GroceryListRepository;
import com.example.fresco.grocerylist.repository.GroceryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

//    public GroceryItem updatePurchased(Long id, Boolean purchased){
//        GroceryItem item = groceryListRepository.findById(id)
//                .orElseThrow(()-> new RuntimeException("Item not found"));
//        item.updatePurchased(purchased);
//        return groceryListRepository.save(item);
//    }
//
//    public GroceryItem updateName(Long id, String name){
//        GroceryItem item = groceryListRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Item not found"));
//        item.updateName(name);
//        return groceryListRepository.save(item);
//    }

    public void deleteItem(Long id){
        groceryListRepository.deleteById(id);
    }
}
