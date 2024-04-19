package kr.game.sale.controller;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewDTO;
import kr.game.sale.entity.game.review.ReviewPageDTO;
import kr.game.sale.entity.game.review.ReviewResponse;
import kr.game.sale.entity.game.review.report.ReviewReportDTO;
import kr.game.sale.entity.game.review.vote.ReviewVoteDTO;
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
        Users user = userService.getLoggedInUser();
        if(user == null) return "LOGIN_REQUIRED";
        //log.info(" reviewDTO =>{}"+ reviewDTO);
        Game game =  gameService.findOneById(Long.valueOf(reviewDTO.getAppId()));

        return  gameReviewService.saveReview(Review.builder()
                .isPositive(reviewDTO.getIsPositive())
                .content(reviewDTO.getContent())
                .localDateTime(LocalDateTime.now())
                .user(user)
                .game(game)
                .build()
        );
    }

    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> listGameReview(@RequestBody ReviewPageDTO reviewPageDTO) {
        //log.info(" reviewPageDTO =>{}"+ reviewPageDTO);
        Pageable pageable = PageRequest.of(reviewPageDTO.getCurrPage()-1, reviewPageDTO.getPageSize(), Sort.by("ID").descending());

        Users user = userService.getLoggedInUser();
        if(user!= null){
            reviewPageDTO.setUserId(user.getId());
        }
        Page<ReviewResponse> list = gameReviewService.findReviewsAndVoteByUserId(reviewPageDTO,pageable);
        if(list.getContent().size() == 0){
            log.error("리스트가 비었습니다.");
        }
        for(ReviewResponse r : list){
            log.info(" Review =>{}"+ r);
        }
        reviewPageDTO.setPageCxt(reviewPageDTO.getCurrPage(), list.getTotalPages());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("reviewPageDTO", reviewPageDTO);
        dataMap.put("list",list.getContent());

        return dataMap;
    }

    @PostMapping("/vote")
    @ResponseBody
    public String voteGameReview(@RequestBody ReviewVoteDTO reviewVoteDTO) {
        // gameSearchDTO를 사용하여 검색 로직을 수행하고 결과를 생성합니다.
        // 이 예시에서는 받은 DTO 객체를 문자열로 반환합니다.
        Users user = userService.getLoggedInUser();
        if(user == null){
            return "LOGIN_REQUIRED";
        }else{
            reviewVoteDTO.setUserId(user.getId());
        }

        if(gameReviewService.findReviewVoteByUserId(user.getId(), Long.valueOf(reviewVoteDTO.getReviewId())) != null) return  "ALREADY_VOTED";

        //log.info(" reviewVoteDTO =>{}"+ reviewVoteDTO);
        String result =  gameReviewService.saveReviewVote(reviewVoteDTO);
        //log.info(" voteGameReview result =>{}"+ result);
        return result;
    }

    @PostMapping("/report")
    @ResponseBody
    public String reportGameReview(@RequestBody ReviewReportDTO reviewReportDTO) {
        // gameSearchDTO를 사용하여 검색 로직을 수행하고 결과를 생성합니다.
        // 이 예시에서는 받은 DTO 객체를 문자열로 반환합니다.
        Users user = userService.getLoggedInUser();
        if(user == null){
            return "LOGIN_REQUIRED";
        }else{
            reviewReportDTO.setUserId(user.getId());
        }
        //log.info(" reviewReportDTO =>{}"+ reviewReportDTO);
        String result =  gameReviewService.saveReviewReport(reviewReportDTO);
        //log.info(" reportGameReview result =>{}"+ result);
        return result;
    }


}
