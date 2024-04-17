package kr.game.sale.repository.game;

import kr.game.sale.entity.game.Game;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
    Game findBySteamAppid(Long steamAppid);
}
