package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.SortType;
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

    public List<Game> findMainList(String sortType){
        List<Game> result = null;
        switch (sortType){
            case "최신순":
                result = gameRepository.findMainList(SortType.REG_DATE, new GameSearchCondition());
                break;
            case "할인율순":
                result =gameRepository.findMainList(SortType.DISCOUNT, new GameSearchCondition());
                break;
            case "한국어":
                GameSearchCondition conditon = new GameSearchCondition();
                conditon.setLanguage("한국");
                result =gameRepository.findMainList(SortType.REG_DATE, conditon);
                 break;
            case "인기순":
                result =gameRepository.findMainList(SortType.POPULARITY, new GameSearchCondition());
                 break;
        }
        return result;
    }



}
