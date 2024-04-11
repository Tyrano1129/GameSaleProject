package kr.game.sale.repository.game;

import kr.game.sale.entity.game.Game;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
    Game findBySteamAppid(int steamAppid);
}
