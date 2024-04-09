package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.repository.game.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public void initData() throws JsonProcessingException {
        List<Game> list = gameRepository.saveGameData();
        gameRepository.saveAll(list);
    }

    public List<Game> getList(){
       return gameRepository.findAll();
    }



}
