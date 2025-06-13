package fresco.com.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private List<MemberRole> roles;

    public Member(String name, String email, List<MemberRole> roles) {
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public static Member of(String name, String email, List<MemberRole> roles) {
        return new Member(name, email, roles);
    }
}
