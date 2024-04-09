package kr.game.sale.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final GameService gameService;

    @GetMapping({"/",""})
    public String home(Model model){
        List<Game> list = null;
        try {
            log.trace("gameService getList is null =>{}",gameService.getList()== null);
            if (gameService.getList() != null) { // reg date로 확인하는 로직 추가 예정
                gameService.initData();
            }
            list = gameService.getList();
        } catch (JsonProcessingException e) {
            log.error("initData Error => {}", e.getMessage());
        }


        model.addAttribute("list",list );
        return "main";

    }

}
