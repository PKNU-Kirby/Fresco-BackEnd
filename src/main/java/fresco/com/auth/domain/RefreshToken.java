package fresco.com.auth.domain;

import fresco.com.member.domain.Member;
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
    @JoinColumn(name = "ACCOUNT_UNIQUE_ID", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public RefreshToken(Member member) {
        this.member = member;
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
