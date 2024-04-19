package kr.game.sale.controller;

import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.entity.form.GameForm;
import kr.game.sale.entity.form.QnAAmdinForm;
import kr.game.sale.entity.form.RoleListForm;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        List<Game> gameList = gameService.getList();
        List<Users> userList = userService.getUserList();
        List<QnA> qnaList = adminService.getQnAList();
        List<Refund> refundList = adminService.getRefundList();
        List<Review> reviewsList = gameReviewService.findAllReportedReviews();
        for(Refund list : refundList){
            String[] payment = list.getPaymentIds().split(",");
            List<Payment> pays = new ArrayList<>();
            for(String pay : payment){
                Payment p = adminService.getOnePaymet(Long.parseLong(pay));
                pays.add(p);
            }
            list.setPaymentList(pays);
        }
        model.addAttribute("gameList", gameList);
        model.addAttribute("userList", userList);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("refundList", refundList);
        model.addAttribute("reviewsList", reviewsList);
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

}
