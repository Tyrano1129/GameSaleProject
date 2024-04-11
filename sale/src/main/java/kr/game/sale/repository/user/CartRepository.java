package kr.game.sale.repository.user;

import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUsers(Users users);
}
