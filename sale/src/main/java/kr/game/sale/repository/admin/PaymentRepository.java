package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
