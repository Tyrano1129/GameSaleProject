package kr.game.sale.controller;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.GameReviewService;
import kr.game.sale.service.GameService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final GameReviewService reviewService;

    @GetMapping
    public String gameForm(){
        return "game/gameForm";
    }


    @PostMapping("/keywordSearch")
    @ResponseBody
    public Map<String, Object> handleGameKeywordSearch(@RequestBody GameSearchDTO gameSearchDTO) {
        // gameSearchDTO를 사용하여 검색 로직을 수행하고 결과를 생성합니다.
        // 이 예시에서는 받은 DTO 객체를 문자열로 반환합니다.
        //log.info("gameSearchDTO =>{}"+gameSearchDTO);
        Pageable pageable = PageRequest.of(gameSearchDTO.getCurrPage()-1, gameSearchDTO.getPageSize(), Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(gameSearchDTO,pageable);

        gameSearchDTO.setPageCxt(gameSearchDTO.getCurrPage(), list.getTotalPages());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gameSearchDTO", gameSearchDTO);
        dataMap.put("list",list.getContent());

        return dataMap;
    }
    @GetMapping("/search/fromBar")
    public String searchFormBar(@RequestParam("keyword")String keyword, Model model){

        //log.info("searchFormBar 실행");
        //log.info("keyword =>{}",keyword);
        GameSearchDTO dto = new GameSearchDTO();
        dto.setSearchKeyword(keyword);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(dto,pageable);
        List<String> publishers = gameService.findAllPublishers();
        dto.setPageCxt(1, list.getTotalPages());

       // log.info("gameSearchDTO =>{}"+dto);
        model.addAttribute("gameSearchDTO", dto);
        model.addAttribute("list",list);
        model.addAttribute("publishers",publishers);

        return "game/gameSearch";
    }

    @GetMapping("/categorySearch/{category}")
    public String categorySearch(@PathVariable("category") String category, Model model){

        GameSearchDTO dto = new GameSearchDTO();
        dto.setSearchCategory(category);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(dto,pageable);
        List<String> publishers = gameService.findAllPublishers();
        dto.setPageCxt(1, list.getTotalPages());

        // log.info("gameSearchDTO =>{}"+dto);
        model.addAttribute("gameSearchDTO", dto);
        model.addAttribute("list",list);
        model.addAttribute("publishers",publishers);

        return "game/gameSearch";
    }


    @GetMapping("/search/fromMain")
    public String searchMain(Model model){
        //log.info("findMainList(\"최신순\") 실행");

        GameSearchDTO dto = new GameSearchDTO();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(dto,pageable);

        List<String> publishers = gameService.findAllPublishers();

        dto.setPageCxt(1, list.getTotalPages());

        model.addAttribute("gameSearchDTO", dto);
        model.addAttribute("list",list);
        model.addAttribute("publishers",publishers);
        return "game/gameSearch";
    }

    @GetMapping("/detail/{steamAppid}")
    public String gameDetail(@PathVariable("steamAppid") String steamAppid, Model model){

        Game game = gameService.findOneById(Long.valueOf(steamAppid));
        Users user = userService.getLoggedInUser();

        if(user != null){
            Review review =reviewService.findReviewByUserId(user.getId(), game.getSteamAppid());
            if (review != null){
                user = null;
            }
        }

        if(game != null){
            //log.info("found game =>{}",game);
            model.addAttribute("game",game);
            model.addAttribute("user",user);
            return "game/gameDetail";
        }else{
            return "redirect:/";
        }
    }
}
