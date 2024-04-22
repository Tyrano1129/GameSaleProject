package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Long>, RefundCusRepository {
}
