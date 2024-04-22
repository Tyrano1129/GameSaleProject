package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.SortType;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.repository.game.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private boolean saveDataEvent =false;

    public void initData() throws JsonProcessingException {
        if(saveDataEvent == true)return;
        saveDataEvent = true;
        List<Game> list = gameRepository.saveGameData();
        gameRepository.saveAll(list);
        saveDataEvent = false;
    }


    public Game findOneById(Long id){
        Game game = gameRepository.findBySteamAppid(id);
        if(game!= null){
            return game;
        }else{
            return null;
        }
    }

    public List<String> findAllPublishers() {return gameRepository.findAllPublishers(); }
    public List<Game> getList(){
       return gameRepository.findAll();
    }
    public Page<Game> searchGamesByKeyword(GameSearchDTO gameSearchDTO, Pageable pageable){return gameRepository.searchGamesByKeyword(gameSearchDTO, pageable);}

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

    public void gameOneDelete(Long id){
        gameRepository.deleteById(id);
    }

    public Page<Game> gameListPage(Pageable pageable){
        return gameRepository.serchAdminGameList(pageable);
    }

}
