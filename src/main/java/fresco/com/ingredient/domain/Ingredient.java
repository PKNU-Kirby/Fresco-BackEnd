package fresco.com.ingredient.domain;

import fresco.com.global.domain.BaseEntity;
import fresco.com.refrigerator.domain.Refrigerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "refrigeratorId")
    private Refrigerator refrigerator;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    String name;
    Date expirationDate;
    Integer quantity;

    public Ingredient(Refrigerator refrigerator, Category category, String name, Date expirationDate, Integer quantity) {
        this.refrigerator = refrigerator;
        this.category = category;
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }
}