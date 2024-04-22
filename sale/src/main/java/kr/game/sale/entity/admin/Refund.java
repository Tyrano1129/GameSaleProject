package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Setter
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
    @JsonIgnore
    @OneToMany
    @Transient
    private List<Payment> paymentList;
    private String refundViewDate;


    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void setRefundWhether(boolean refundWhether) {
        this.refundWhether = refundWhether;
    }
    public boolean getRefundWhether(){
        return this.refundWhether;
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
