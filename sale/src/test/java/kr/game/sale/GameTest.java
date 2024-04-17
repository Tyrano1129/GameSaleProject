package kr.game.sale;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewResponse;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.GameReviewService;
import kr.game.sale.service.GameService;
import kr.game.sale.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GameTest {
/*
    private UserService userService;
    private GameService gameService;
    private GameReviewService gameReviewService;
    @Autowired
    GameTest(GameService gameservice, GameReviewService gamereviewservice, UserService userservice) {
        this.userService = userservice;
        this.gameService = gameservice;
        this.gameReviewService = gamereviewservice;
    }

    @Test
    public void removeTest(){


        List<ReviewResponse> list = gameReviewService.findReviewsAndVoteByUserId(1L,1623730L);

        for (ReviewResponse r : list){
            System.out.println(r.getReviewId());
            if(r.getReviewVoteId() != 0){
                System.out.println(r.getReviewVoteId() +" <- 회원 리뷴");
            }
        }

        try {
            gameService.initData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Game game =  gameService.findOneById("1086940");

        Users user = userService.getOneUsers(1L);
        gameReviewService.saveReview(Review.builder()
                .isPositive(true)
                .content("리뷰테스트")
                .localDateTime(LocalDateTime.now())
               .user(user)
                .game(game)
                .build());


        userService.adminUsersOneDelete(1l);
    }*/


//    @Test
//    public void initData(){
//        try {
//            gameService.initData();
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        Game game =  gameService.findOneById("1086940");
//
//        Users user = userService.getOneUsers(1L);
//        gameReviewService.saveReview(Review.builder()
//                .isPositive(true)
//                .content("리뷰테스트")
//                .localDateTime(LocalDateTime.now())
//                .user(user)
//                .game(game)
//                .build());
//
//        ReviewPageDTO reviewPageDTO = new ReviewPageDTO();
//        reviewPageDTO.setCurrPage(1);
//        reviewPageDTO.setAppId("1086940");
//
//        Pageable pageable = PageRequest.of(reviewPageDTO.getCurrPage()-1, reviewPageDTO.getPageSize(), Sort.by("reviewId").descending());
//        Page<Review> list = gameReviewService.getList(reviewPageDTO,pageable);
//        if(list.getContent().size() == 0){
//            System.out.println("리스트가 비었습니다.");
//        }
//        for(Review r : list.getContent()){
//            System.out.println(" Review =>{}"+ r.getGame().getSteamAppid());
//        }
//    }
//
//    @Test
//    public void ReviewListTest(){

}



