package kr.game.sale.controller;

import kr.game.sale.entity.game.Game;
import kr.game.sale.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @GetMapping
    public String gameForm(){
        return "game/gameForm";
    }


    @GetMapping("/detail/{steamAppid}")
    public String gameDetail(@PathVariable("steamAppid") String steamAppid, Model model){
        Game game = gameService.findOneById(steamAppid);
        if(game != null){
            log.info("found game =>{}",game);
            model.addAttribute("game",game);
            return "game/gameDetail";
        }else{
            return "redirect:/";
        }
    }
}
