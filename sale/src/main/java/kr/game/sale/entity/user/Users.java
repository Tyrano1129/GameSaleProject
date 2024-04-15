package kr.game.sale.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kr.game.sale.entity.game.review.Review;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import kr.game.sale.entity.admin.QnA;

@Entity
@Data
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Transient
    private List<Cart> carts;

    // QnA 와의 일대다 관계 설정
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Transient
    private List<QnA> qnas;
}
