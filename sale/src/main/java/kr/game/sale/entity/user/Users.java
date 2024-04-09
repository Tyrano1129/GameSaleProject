package kr.game.sale.entity.user;

import jakarta.persistence.*;
import lombok.Data;

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
}
