package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="gaem_id")
    private Game game;
    private LocalDateTime paymenDate;
    private String gameName;
    private int gamePrice;
    private int paymentPrice;
    private String paymentOrdernum; // MerchantUid
    private String gameCode;
    private boolean paymentResult; // 환불 요청여부
    public Payment(boolean paymentResult) {
        this.paymentResult = paymentResult;
    }

    @Builder
    public Payment(Users user, Game game, String gameName, int gamePrice, int paymentPrice, String paymentOrdernum,String gameCode) {
        this.user = user;
        this.game = game;
        this.paymenDate = LocalDateTime.now();
        this.gameName = gameName;
        this.gamePrice = gamePrice;
        this.paymentPrice = paymentPrice;
        this.paymentOrdernum = paymentOrdernum;
        this.gameCode = gameCode;
    }
    public String localDateFormater(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formated = this.paymenDate.format(formatter);
        return formated;
    }
}
