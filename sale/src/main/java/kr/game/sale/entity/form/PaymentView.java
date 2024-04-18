package kr.game.sale.entity.form;

import kr.game.sale.entity.admin.Payment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class PaymentView {
    private String ordernum;
    private List<Payment> paymentList;
    private String paymentResult;
}
