package kr.game.sale.repository.user;

import kr.game.sale.entity.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCusRepository {
   Page<Users> searchUser(Pageable pageable);
}
