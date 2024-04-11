package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeCusRepository {
    int countAllBy();
}
