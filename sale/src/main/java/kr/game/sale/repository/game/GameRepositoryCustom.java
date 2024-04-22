package kr.game.sale.repository.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GameRepositoryCustom {
    List<Game> saveGameData() throws JsonProcessingException;

    List<Game> findMainList(SortType type, GameSearchCondition condition);
    public List<String> findAllPublishers();
    public Page<Game> searchGamesByKeyword(GameSearchDTO gameSearchDTO, Pageable pageable);
    Page<Game> serchAdminGameList(Pageable pageable);
}
