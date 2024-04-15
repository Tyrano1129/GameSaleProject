package kr.game.sale.entity.user;

import jakarta.persistence.*;
import kr.game.sale.entity.admin.QnA;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;
    private String userNickname;
    private String userPhone;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    // OAuth 를 위해 추가하는 필드
    private String provider;
    private String providerId;

    // Cart 와의 일대다 관계 설정
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Cart> carts;

    // QnA 와의 일대다 관계 설정
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<QnA> qnas;
}
