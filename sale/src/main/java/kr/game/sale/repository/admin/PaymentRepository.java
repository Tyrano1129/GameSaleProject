package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    void deleteAllByPaymentOrdernum(String paymentOrdernum);

    List<Payment> findAllByUser(Users user);

    List<Payment> findAllByPaymentOrdernum(String paymentOrdernum);
}
