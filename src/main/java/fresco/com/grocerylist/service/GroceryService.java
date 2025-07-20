package fresco.com.grocerylist.service;

import fresco.com.grocerylist.domain.Grocery;
import fresco.com.grocerylist.repository.GroceryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroceryService {
    private final GroceryRepository groceryRepository;

    public Grocery addItem(String name){
        return groceryRepository.save(Grocery.builder()
                .name(name)
                .purchased(false)
                .build());
    }

    public List<Grocery> getAllItems(){
        return groceryRepository.findAll();
    }

    public Grocery updatePurchased(Long id, boolean purchased){
        Grocery item = groceryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Item not found"));
        item.setPurchased(purchased);
        return groceryRepository.save(item);
    }

    public Grocery updateName(Long id, String name){
        Grocery item = groceryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(name);
        return groceryRepository.save(item);
    }

    public void deleteItem(Long id){
        groceryRepository.deleteById(id);
    }
}
