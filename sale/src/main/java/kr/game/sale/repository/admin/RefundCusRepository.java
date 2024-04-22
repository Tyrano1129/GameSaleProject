package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RefundCusRepository {
    Page<Refund> serchRefund(Pageable pageable);
}
