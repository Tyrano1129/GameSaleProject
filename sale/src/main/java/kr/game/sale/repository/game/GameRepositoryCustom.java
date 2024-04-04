package kr.game.sale.repository.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;

import java.util.List;

public interface GameRepositoryCustom {
    List<Game> saveGameData() throws JsonProcessingException;
}
