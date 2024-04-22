package kr.game.sale.entity.user;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
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

//    // Cart 와의 일대다 관계 설정
//    @OneToMany(fetch = FetchType.LAZY)
//    @Transient
//    private List<Cart> carts;
//
//    // QnA 와의 일대다 관계 설정
//    @OneToMany(fetch = FetchType.LAZY)
//    @Transient
//    private List<QnA> qnas;


    @QueryProjection
    public Users(Long id, String username, String password, String userNickname, String userPhone, UserRole userRole, String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
        this.userRole = userRole;
        this.provider = provider;
        this.providerId = providerId;
    }

}
