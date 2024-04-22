package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnACusRepository {
    Page<QnA> serachQnA(Pageable pageable);
}
