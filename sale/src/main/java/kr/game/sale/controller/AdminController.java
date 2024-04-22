package kr.game.sale.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.entity.form.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    private final GoogleGCPService googleGCPService;
    private final GameReviewService gameReviewService;

    @Value("${imp.api.key}")
    private String apiKey;
    @Value("${imp.api.secretkey}")
    private String secretKey;

    private void init(Model model) {
        Users user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        //gameForm 여러곳에 이용하기위한 객체 생성
    }

    @GetMapping
    public String adminForm(Model model) {
        init(model);
        return "admin/adminForm";
    }

    @GetMapping("/gameForm")
    public String getGameForm(Model model) {
        init(model);
        model.addAttribute("formUpdate","gameInsert");
        return "admin/gameForm";
    }

    @GetMapping("/gameUpdateForm")
    public String getUpdateForm(@RequestParam(name = "id") Long id, Model model) {
        Game game = gameService.findOneById(id);
        init(model);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = game.getGenreList();
        String genres = "";
        for(int i =0; i < list.size(); i+=1){
            genres += i == 0? list.get(i) : "," + list.get(i);
        }
        GameForm games = GameForm.builder()
                .id(game.getSteamAppid())
                .name(game.getName())
                .supportedLanguages(game.getSupportedLanguages())
                .price(game.getPrice())
                .viewprice(game.getFormattedPrice())
                .developers(game.getDevelopers())
                .releaseDate(formatter.format(game.getReleaseDate()))
                .genres(genres)
                .minRequirements(game.getMinRequirements())
                .rcmRequirements(game.getRcmRequirements())
                .headerImage(game.getHeaderImage())
                .screenshots(game.getScreenshootsList())
                .stock(game.getStock())
                .platform(game.getPlatform())
                .build();

        String update = "/admin/gameUpdate";
        Users user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("game", games);
        model.addAttribute("update", update);
        model.addAttribute("formUpdate","gameupdate");

        return "admin/gameForm";
    }


    @DeleteMapping("/gameOneDelete")
    public @ResponseBody String gameOneDelete(Long id) {
        gameService.gameOneDelete(id);
        return "ok";
    }

    @DeleteMapping("/reviewOneDelete")
    public @ResponseBody String reviewOneDelete(Long id) {
        gameReviewService.deleteReview(id);
        return "ok";
    }

    @PostMapping("/userListRoleUpdate")
    public @ResponseBody String userListRoleUpdate(@RequestBody List<RoleListForm> roleList) {
        Users user = userService.getLoggedInUser();
        if(user.getUserRole().toString().equals("ROLE_ADMIN")){
            System.out.println("roleList = " + roleList);
            for (RoleListForm list : roleList) {
                userService.userRoleUpdate(list);
            }
        }
        return "ok";
    }

    @DeleteMapping("/userOneDelete")
    public @ResponseBody String userOneDate(Long id) {
        Users user = userService.getLoggedInUser();
        if(user.getUserRole().toString().equals("ROLE_ADMIN")){
            userService.adminUsersOneDelete(id);
        }
        return "ok";
    }

    @DeleteMapping("/paymentOneDelete")
    public @ResponseBody String paymentOneDelete(Long id) throws IOException {
        Refund refund = adminService.getOneRefund(id);
        RuntimeException e = new RuntimeException();
        if (refund != null) {
            String[] payment = refund.getPaymentIds().split(",");
//            log.info("payment={}",payment.toString());
            List<Payment> pays = new ArrayList<>();
            for(String pay : payment){
                Payment p = adminService.getOnePaymet(Long.parseLong(pay));
                pays.add(p);
            }
            refund.setPaymentList(pays);
            String token = adminService.getToken(apiKey, secretKey);
            adminService.refundRequest(token, refund.getPaymentList().get(0).getPaymentOrdernum(), e.getMessage(), 0);
            adminService.paymentUpdate(refund.getPaymentList().get(0).getPaymentId(), refund);
        }
        return "ok";
    }

    @PostMapping("/gameInsert")
    public String gmaeInsert(@ModelAttribute GameForm game) throws IOException, ParseException {
//        log.info("game={}", game);
        List<String> screenshotsList = new ArrayList<>();
        String[] genres = game.getGenres().split(",");
        List<String> genresList = new ArrayList<>(Arrays.asList(genres));
        String headerImage = null;
        if(game.getHeaderFile() != null){
            headerImage = googleGCPService.updateImageInfo(game.getHeaderFile(), "game");
        }
        if(game.getScreenFile() != null) {
            for (MultipartFile file : game.getScreenFile()) {
                String screanshots = null;
                if (file != null) {
                    screanshots = googleGCPService.updateImageInfo(file, "game");
                }
                screenshotsList.add(screanshots);
            }
        }
        adminService.gameInsert(game, headerImage, screenshotsList, genresList);
        return "redirect:/admin";
    }

    @PostMapping("/gameUpdate")
    public String gameUpdate(@ModelAttribute GameForm game) throws IOException, ParseException {
//        log.info("game={}", game);
        adminService.gameUpdate(game);
        return "redirect:/admin";
    }

    @PutMapping("/qnaOneUpdate")
    public @ResponseBody String qnaOneUpdate(@RequestBody QnAAmdinForm admin) {
//        log.info("admin={}", admin);
        adminService.qnaOneUpdate(admin);
        return "ok";
    }

    @GetMapping("/list")
    public @ResponseBody String getList(@RequestParam("type") String type) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0,5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(type.equals("userList")){
            Page<Users> usersPage = userService.userListPageing(pageable);
            AdminPageList<Users> usersAdminPageList = new AdminPageList<>();
            // 해당 비동기
            usersAdminPageList.setContnet(usersPage.getContent());
            usersAdminPageList.setPageCxt(usersPage.getNumber(),usersPage.getTotalPages());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

//            log.info("list = {}",gson.toJson(usersAdminPageList));
            return gson.toJson(usersAdminPageList);
        }else if(type.equals("qnaList")){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            Page<QnA> qnaPage = adminService.qnaListPageing(pageable);
            AdminPageList<QnA> qnAAdminPageList = new AdminPageList<>();
            for(QnA qna : qnaPage.getContent()){
                qna.setDateView(formatter.format(qna.getLocalDateTime()));
            }
            qnAAdminPageList.setContnet(qnaPage.getContent());
            qnAAdminPageList.setPageCxt(qnaPage.getNumber(),qnaPage.getTotalPages());
            qnAAdminPageList.setPage(qnaPage.getNumber());

//            log.info("test ={}",objectMapper.writeValueAsString(qnAAdminPageList));
            return objectMapper.writeValueAsString(qnAAdminPageList);
        }else if(type.equals("gameList")){
            ObjectMapper objectMapper = new ObjectMapper();
            // Jackson 라이브러리 사용하여 localdatetime 을 직렬화 역직렬화함.
            objectMapper.registerModule(new JavaTimeModule());
            // 날짜를 타임스탬프로 직렬화하지 않도록 설정
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Page<Game> gamePage = gameService.gameListPage(pageable);
            AdminPageList<Game> gameAdminPageList = new AdminPageList<>();
            for(Game game : gamePage.getContent()){
                if(game.getReleaseDate() == null){
                    continue;
                }
                game.setPriceView(game.getFormattedPrice());
                game.setGameDate(format.format(game.getReleaseDate()));
            }
            gameAdminPageList.setContnet(gamePage.getContent());
            gameAdminPageList.setPageCxt(gamePage.getNumber(),gamePage.getTotalPages());

//            log.info("test ={}",objectMapper.writeValueAsString(gameAdminPageList));
            return objectMapper.writeValueAsString(gameAdminPageList);
        }else if(type.equals("refundList")){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            Page<Refund> refundPage = adminService.refundListPageing(pageable);
            for(Refund list : refundPage.getContent()){
                String[] payment = list.getPaymentIds().split(",");
                List<Payment> pays = new ArrayList<>();
                for(String pay : payment){
                    Payment p = adminService.getOnePaymet(Long.parseLong(pay));
                    pays.add(p);
                }
                list.setRefundViewDate(formatter.format(list.getRefundAplctdate()));
                list.setPaymentList(pays);
            }
            AdminPageList<Refund> refundAdminPageList = new AdminPageList<>();

            refundAdminPageList.setContnet(refundPage.getContent());
            refundAdminPageList.setPageCxt(refundPage.getNumber(),refundPage.getTotalPages());
//            log.info("test ={}",objectMapper.writeValueAsString(refundAdminPageList));
            return objectMapper.writeValueAsString(refundAdminPageList);
        }else if(type.equals("reviewList")){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            Page<Review> reviewPage = gameReviewService.reviewsListPage(pageable);
            AdminPageList<Review> reviewAdminPageList = new AdminPageList<>();

            for(Review review : reviewPage.getContent()){
                review.setReviewDateView(formatter.format(review.getRegDate()));
            }
            reviewAdminPageList.setContnet(reviewPage.getContent());
            reviewAdminPageList.setPageCxt(reviewPage.getNumber(),reviewPage.getTotalPages());
//            log.info("test ={}",objectMapper.writeValueAsString(reviewAdminPageList));
            return objectMapper.writeValueAsString(reviewAdminPageList);
        }
        return null;
    }

    @GetMapping("/userPageList")
    public @ResponseBody String getPageList(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Users> usersPage = userService.userListPageing(pageable);
        AdminPageList<Users> usersAdminPageList = new AdminPageList<>();
        // 해당 비동기
        usersAdminPageList.setContnet(usersPage.getContent());
        usersAdminPageList.setPageCxt(usersPage.getNumber(),usersPage.getTotalPages());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

//        log.info("list = {}",gson.toJson(usersAdminPageList));
        return gson.toJson(usersAdminPageList);
    }

    @GetMapping("/qnaPageList")
    public @ResponseBody String getqnaPageList(int page, int size) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Pageable pageable = PageRequest.of(page,size);
        Page<QnA> qnaPage = adminService.qnaListPageing(pageable);
        AdminPageList<QnA> qnAAdminPageList = new AdminPageList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(QnA qna : qnaPage.getContent()){
            qna.setDateView(formatter.format(qna.getLocalDateTime()));
        }
        qnAAdminPageList.setContnet(qnaPage.getContent());
        qnAAdminPageList.setPageCxt(qnaPage.getNumber(),qnaPage.getTotalPages());

//        log.info("test ={}",objectMapper.writeValueAsString(qnAAdminPageList));
        return objectMapper.writeValueAsString(qnAAdminPageList);
    }

    @GetMapping("/gamePageList")
    public @ResponseBody String getGamePageList(int page,int size) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Pageable pageable = PageRequest.of(page,size);
        Page<Game> gamePage = gameService.gameListPage(pageable);
        AdminPageList<Game> gameAdminPageList = new AdminPageList<>();
        // 해당 비동기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(Game game : gamePage.getContent()){
            if(game.getReleaseDate() == null){
                continue;
            }
            game.setPriceView(game.getFormattedPrice());
            game.setGameDate(format.format(game.getReleaseDate()));
        }
        gameAdminPageList.setContnet(gamePage.getContent());
        gameAdminPageList.setPageCxt(gamePage.getNumber(),gamePage.getTotalPages());

//        log.info("test ={}",objectMapper.writeValueAsString(gameAdminPageList));
        return objectMapper.writeValueAsString(gameAdminPageList);
    }

    @GetMapping("/refundPageList")
    public @ResponseBody String getRefundPageList(int page,int size) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Pageable pageable = PageRequest.of(page,size);
        Page<Refund> refundPage = adminService.refundListPageing(pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Refund list : refundPage.getContent()){
            String[] payment = list.getPaymentIds().split(",");
            List<Payment> pays = new ArrayList<>();
            for(String pay : payment){
                Payment p = adminService.getOnePaymet(Long.parseLong(pay));
                pays.add(p);
            }
            list.setRefundViewDate(formatter.format(list.getRefundAplctdate()));
            list.setPaymentList(pays);
        }
        AdminPageList<Refund> refundAdminPageList = new AdminPageList<>();

        refundAdminPageList.setContnet(refundPage.getContent());
        refundAdminPageList.setPageCxt(refundPage.getNumber(),refundPage.getTotalPages());
//        log.info("test ={}",objectMapper.writeValueAsString(refundAdminPageList));
        return objectMapper.writeValueAsString(refundAdminPageList);
    }

    @GetMapping("/getReviewPageList")
    public @ResponseBody String getReviewPageList(int page,int size) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Pageable pageable = PageRequest.of(page,size);
        Page<Review> reviewPage = gameReviewService.reviewsListPage(pageable);
        AdminPageList<Review> reviewAdminPageList = new AdminPageList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Review review : reviewPage.getContent()){
            review.setReviewDateView(formatter.format(review.getRegDate()));
        }

        reviewAdminPageList.setContnet(reviewPage.getContent());
        reviewAdminPageList.setPageCxt(reviewPage.getNumber(),reviewPage.getTotalPages());
//        log.info("test ={}",objectMapper.writeValueAsString(reviewAdminPageList));
        return objectMapper.writeValueAsString(reviewAdminPageList);
    }
}
