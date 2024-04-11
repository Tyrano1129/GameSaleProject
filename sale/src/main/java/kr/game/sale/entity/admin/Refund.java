package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    private String refundReason;
    private LocalDateTime refundAplctdate;
    private boolean refundWhether;

    public Refund(Payment payment, String refundReason, boolean refundWhether) {
        this.payment = payment;
        this.refundReason = refundReason;
        this.refundAplctdate = LocalDateTime.now();
        this.refundWhether = refundWhether;
    }
}
