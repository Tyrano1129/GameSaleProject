package kr.game.sale.entity.form;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PaymentForm {
    private String username;
    private String gameName;
    private int gamePrice;
    private int paymentPirce;
    private String ordernum;
}

