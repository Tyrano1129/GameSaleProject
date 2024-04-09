package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCusRepository {

    Page<Notice> searchPageSimple(Pageable pageable,String title);

}
