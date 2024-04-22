package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.text.DecimalFormat;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="gaem_id")
    private Game game;
    private LocalDateTime paymenDate;
    private String gameName;
    private int gamePrice;
    private int paymentPrice;
    private String paymentOrdernum; // MerchantUid
    private String gameCode;
    private String paymentResult; // 환불 요청여부
    public void setPaymentResult(String paymentResult) {
        this.paymentResult = paymentResult;
    }

    @Builder
    public Payment(Users user, Game game, String gameName, int gamePrice, int paymentPrice, String paymentOrdernum,String gameCode,String paymentResult) {
        this.user = user;
        this.game = game;
        this.paymenDate = LocalDateTime.now();
        this.gameName = gameName;
        this.gamePrice = gamePrice;
        this.paymentPrice = paymentPrice;
        this.paymentOrdernum = paymentOrdernum;
        this.gameCode = gameCode;
        this.paymentResult = paymentResult;
    }
    public String localDateFormater(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formated = this.paymenDate.format(formatter);
        return formated;
    }

}
