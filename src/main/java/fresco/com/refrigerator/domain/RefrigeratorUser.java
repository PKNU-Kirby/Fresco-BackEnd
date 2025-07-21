package fresco.com.refrigerator.domain;

import fresco.com.global.domain.BaseEntity;
import fresco.com.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefrigeratorUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "refrigeratorId")
    private Refrigerator refrigerator;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public RefrigeratorUser(Refrigerator refrigerator, User user) {
        this.refrigerator = refrigerator;
        this.user = user;
    }
}