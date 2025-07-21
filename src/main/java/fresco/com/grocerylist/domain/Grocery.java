package fresco.com.grocerylist.domain;

import jakarta.annotation.Resource;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean purchased;

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePurchased(Boolean newPurchased) {
        this.purchased = newPurchased;
    }
}
