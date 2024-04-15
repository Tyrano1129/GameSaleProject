package kr.game.sale.controller;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewDTO;
import kr.game.sale.entity.game.review.ReviewPageDTO;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gameReview")
public class GameReviewController {
    private final GameService gameService;
    private final UserService userService;
    private final GameReviewService gameReviewService;

    @PostMapping("/insert")
    @ResponseBody
    public String insertGameReview(@RequestBody ReviewDTO reviewDTO) {
        // gameSearchDTO를 사용하여 검색 로직을 수행하고 결과를 생성합니다.
        // 이 예시에서는 받은 DTO 객체를 문자열로 반환합니다.
        log.info(" reviewDTO =>{}"+ reviewDTO);
        Game game =  gameService.findOneById(reviewDTO.getAppId());

        Users user = userService.getOneUsers(1L);
        gameReviewService.saveReview(Review.builder()
                .isPositive(reviewDTO.getIsPositive())
                .content(reviewDTO.getContent())
                .localDateTime(LocalDateTime.now())
                .user(user)
                .game(game)
                .build()
        );


        return "success";
    }

    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> listGameReview(@RequestBody ReviewPageDTO reviewPageDTO) {
        log.info(" reviewPageDTO =>{}"+ reviewPageDTO);
        Pageable pageable = PageRequest.of(reviewPageDTO.getCurrPage()-1, reviewPageDTO.getPageSize(), Sort.by("ID").descending());
        Page<Review> list = gameReviewService.getList(reviewPageDTO,pageable);
        if(list.getContent().size() == 0){
            log.error("리스트가 비었습니다.");
        }
        for(Review r : list){
            log.info(" Review =>{}"+ r.getGame().getSteamAppid());
        }
        reviewPageDTO.setPageCxt(reviewPageDTO.getCurrPage(), list.getTotalPages());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("reviewPageDTO", reviewPageDTO);
        dataMap.put("list",list.getContent());

        return dataMap;
    }




    /*
    @GetMapping("/search/fromBar")
    public String searchFormBar(@RequestParam("keyword")String keyword, Model model){

        log.info("searchFormBar 실행");
        log.info("keyword =>{}",keyword);
        GameSearchDTO dto = new GameSearchDTO();
        dto.setSearchKeyword(keyword);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(dto,pageable);
        List<String> publishers = gameService.findAllPublishers();
        for(Game g : list){
            System.out.println(g);
        }
        dto.setPageCxt(1, list.getTotalPages());

        log.info("gameSearchDTO =>{}"+dto);
        model.addAttribute("gameSearchDTO", dto);
        model.addAttribute("list",list);
        model.addAttribute("publishers",publishers);

        return "game/gameSearch";
    }

    @GetMapping("/search/fromMain")
    public String searchMain(Model model){
        log.info("findMainList(\"최신순\") 실행");

        GameSearchDTO dto = new GameSearchDTO();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseDate").ascending());
        Page<Game> list = gameService.searchGamesByKeyword(dto,pageable);

        List<String> publishers = gameService.findAllPublishers();
        for(Game g : list){
            System.out.println(g);
        }
        dto.setPageCxt(1, list.getTotalPages());

        model.addAttribute("gameSearchDTO", dto);
        model.addAttribute("list",list);
        model.addAttribute("publishers",publishers);
        return "game/gameSearch";
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
    }*/
}
