package kr.game.sale.repository.game;

import jakarta.persistence.LockModeType;
import kr.game.sale.entity.game.Game;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
    Game findBySteamAppid(Long steamAppid);

    @Override
    Optional<Game> findById(Long id);
}
