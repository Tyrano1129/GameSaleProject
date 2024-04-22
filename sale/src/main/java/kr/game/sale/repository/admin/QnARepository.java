package kr.game.sale.repository.admin;

import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long>,QnACusRepository {
    List<QnA> findAllByUsers(Users users);
}
