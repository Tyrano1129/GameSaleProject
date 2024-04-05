package kr.game.sale.entity;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users User;
    @ManyToOne
    @JoinColumn(name="game_id")
    private Game gmae;
    private LocalDateTime paymenDate;
    private String gameName;
    private int gamePrice;
    private int paymentPrice;
    private double paymentDiscount;
    private String paymentOrdernum;
}
