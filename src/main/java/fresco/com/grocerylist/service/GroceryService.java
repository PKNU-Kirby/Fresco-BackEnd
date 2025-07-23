package fresco.com.grocerylist.service;

import fresco.com.grocerylist.domain.GroceryItem;
import fresco.com.grocerylist.dto.GroceryDto;
import fresco.com.grocerylist.repository.GroceryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroceryService {
    private final GroceryRepository groceryRepository;

    public GroceryDto addItem(String name){
        GroceryItem groceryItem = new GroceryItem(name, false);
        return GroceryDto.toDto(groceryRepository.save(groceryItem));
    }

    public List<GroceryItem> getAllItems(){
        return groceryRepository.findAll();
    }

    public GroceryItem updatePurchased(Long id, Boolean purchased){
        GroceryItem item = groceryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Item not found"));
        item.updatePurchased(purchased);
        return groceryRepository.save(item);
    }

    public GroceryItem updateName(Long id, String name){
        GroceryItem item = groceryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.updateName(name);
        return groceryRepository.save(item);
    }

    public void deleteItem(Long id){
        groceryRepository.deleteById(id);
    }
}
