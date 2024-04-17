package kr.game.sale.repository.user;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUsers(Users users);

    Optional<Cart> findByIdAndUsers(Long id, Users users);

    Cart findByUsersAndGame(Users users, Game game);
}
