package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;
    private String refundReason;
    private LocalDateTime refundAplctdate;
    private boolean refundWhether;
    private String paymentIds;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Transient
    private List<Payment> paymentList;


    public void setPayment(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void setRefundWhether(boolean refundWhether) {
        this.refundWhether = refundWhether;
    }

    @Builder
    public Refund(List<Payment>  paymentList, String refundReason, boolean refundWhether,String paymentIds) {
        this.paymentList = paymentList;
        this.refundReason = refundReason;
        this.refundAplctdate = LocalDateTime.now();
        this.refundWhether = refundWhether;
        this.paymentIds = paymentIds;
    }
}
