package kr.game.sale;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class GameTest {
    /*
    private final ReviewRepository repository;
private final CartService cartService;
    private UserService userService;
    private GameService gameService;
    private GameReviewService gameReviewService;
    @Autowired
    GameTest(ReviewRepository repository, CartService cartService, GameService gameservice, GameReviewService gamereviewservice, UserService userservice) {
        this.repository = repository;
        this.cartService = cartService;
        this.userService = userservice;
        this.gameService = gameservice;
        this.gameReviewService = gamereviewservice;
    }

    @Test
    public void removeTest2() throws InterruptedException {
        int memberCount = 10;
        ExecutorService executorsService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        for (int i = 0; i < memberCount; i++) {
            executorsService.submit(()->{
                try {
                    cartService.addCart("1");
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println(e.getMessage());
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);
       // Assertions.assertThat(repository.findVoteCntById(1L)).isEqualTo(50);
    }

    @Test
    public void removeTest() throws InterruptedException {
        int memberCount = 50;
        int ticketAmount = 10;

        ExecutorService executorsService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

       */
/* executorsService.execute(()->{
            ReviewVoteDTO dto = new ReviewVoteDTO();
            dto.setReviewId("1");
            repository.addVote(dto);
            latch.countDown();
            *//*
*/
/*try {
                // repository.decreaseVote(1L);
                ReviewVoteDTO dto = new ReviewVoteDTO();
                dto.setReviewId("1");
                repository.addVote(dto);
                int result = repository.findVoteCntById(1L);
                System.out.println("result = " + result);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
                System.out.println(e.getMessage());
            }finally {
                latch.countDown();
            }*//*
*/
/*
        });
        executorsService.execute(()->{
            ReviewVoteDTO dto = new ReviewVoteDTO();
            dto.setReviewId("1");
            repository.addVote(dto);
            latch.countDown();
            *//*
*/
/*try {
                // repository.decreaseVote(1L);
                ReviewVoteDTO dto = new ReviewVoteDTO();
                dto.setReviewId("1");
                repository.addVote(dto);
                int result = repository.findVoteCntById(1L);
                System.out.println("result = " + result);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
                System.out.println(e.getMessage());
            }finally {
                latch.countDown();
            }*//*
*/
/*
        });*//*

        for (int i = 0; i < memberCount; i++) {
            //repository.decreaseVote(1L);
            executorsService.submit(()->{
                try {
                   // repository.decreaseVote(1L);
                    ReviewVoteDTO dto = new ReviewVoteDTO();
                    dto.setReviewId("1");
                    repository.addVote(dto);
                    int result = repository.findVoteCntById(1L);
                    System.out.println("result = " + result);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println(e.getMessage());
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);
        Assertions.assertThat(repository.findVoteCntById(1L)).isEqualTo(50);
    }
*/


        //출처: https://woonys.tistory.com/277 [WOONY's 인사이트:티스토리]


        /*gameReviewService.deleteReview(4L);

        List<Review> list = gameReviewService.findAllReportedReviews();
        for (Review r : list){
            System.out.println(r.getReviewId());
        }*/

        /*List<ReviewResponse> list = gameReviewService.findReviewsAndVoteByUserId(1L,1623730L);

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
    }/**/


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



