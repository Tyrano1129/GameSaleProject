package kr.game.sale;

import org.springframework.boot.test.context.SpringBootTest;

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
    }
*/

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


