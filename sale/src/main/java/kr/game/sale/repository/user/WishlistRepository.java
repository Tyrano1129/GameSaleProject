package kr.game.sale.repository.user;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import kr.game.sale.entity.user.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUsers(Users users);

    Wishlist findByUsersAndGame(Users users, Game game);
}
