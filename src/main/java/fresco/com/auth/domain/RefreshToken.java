package fresco.com.auth.domain;

import fresco.com.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long id;

    @Column
    private String refreshToken;

    @Column
    private boolean isExpired = false;

    @Column
    private LocalDateTime recentLogin = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    public RefreshToken(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void putRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void expire() {
        isExpired = true;
    }

    public boolean isExpired() {
        return isExpired;
    }
}
