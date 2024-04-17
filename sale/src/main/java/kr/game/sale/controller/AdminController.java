package kr.game.sale.controller;

import com.google.cloud.Date;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.entity.form.GameForm;
import kr.game.sale.entity.form.RoleListForm;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.AdminService;
import kr.game.sale.service.GameService;
import kr.game.sale.service.GoogleGCPService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    private final GoogleGCPService googleGCPService;
    private GameForm init(){
        //gameForm 여러곳에 이용하기위한 객체 생성
        return new GameForm();
    }
    @GetMapping
    public String adminForm(Model model){
        List<Game> gameList = gameService.getList();
        List<Users> userList = userService.getUserList();
        List<QnA> qnaList = adminService.getQnAList();
        List<Refund> refundList = adminService.getRefundList();

        model.addAttribute("gameList",gameList);
        model.addAttribute("userList",userList);
        model.addAttribute("qnaList",qnaList);
        model.addAttribute("refundList",refundList);
        return "admin/adminForm";
    }

    @GetMapping("/gameForm")
    public String getGameForm(Model model){
        model.addAttribute("game",init());
        return "admin/gameForm";
    }
    @GetMapping("/gameUpdateForm")
    public String getUpdateForm(@RequestParam(name="id")Long id, Model model){
        Game game = gameService.getOneGames(id);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        GameForm games = GameForm.builder()
                .id(game.getSteamAppid())
                .name(game.getName())
                .supportedLanguages(game.getSupportedLanguages())
                .price(game.getPrice())
                .viewprice(game.getFormattedPrice())
                .developers(game.getDevelopers())
                .releaseDate(formatter.format(game.getReleaseDate()))
                .genres(game.getGenres())
                .minRequirements(game.getMinRequirements())
                .rcmRequirements(game.getRcmRequirements())
                .headerImage(game.getHeaderImage())
                .screenshots(game.getScreenshootsList())
                .stock(game.getStock())
                .build();

        String update = "/admin/gameUpdate";
        model.addAttribute("game",games);
        model.addAttribute("update",update);

        return "admin/gameForm";
    }
    @PostMapping("/gameUpdate")
    public String getUpdate(){

        return "admin/adminForm";
    }
    @DeleteMapping("/gameOneDelete")
    public @ResponseBody String gameOneDelete(Long id){
        gameService.gameOneDelete(id);
        return "ok";
    }

    @PostMapping("/userListRoleUpdate")
    public @ResponseBody String userListRoleUpdate(@RequestBody List<RoleListForm> roleList){
        System.out.println("roleList = " + roleList);
        for(RoleListForm list : roleList){
           userService.userRoleUpdate(list);
        }
        return "ok";
    }

    @DeleteMapping("/userOneDelete")
    public @ResponseBody String userOneDate(Long id){
       userService.adminUsersOneDelete(id);
        return "ok";
    }
    @PostMapping("/image")
    public @ResponseBody Map<String,Object> updateMemberInfo(MultipartRequest request) throws IOException {
        Map<String,Object> responseData = new HashMap<>();
//        String gcpUrl = .updateMemberInfo(request);
//        responseData.put("uploaded",true);
//        responseData.put("url",gcpUrl);
        return responseData;
    }
    @PostMapping("/gameInsert")
    public  String gmaeInsert(@ModelAttribute GameForm game) throws IOException, ParseException {
        log.info("game={}",game);
        List<String> screenshotsList = new ArrayList<>();
        String headerImage = googleGCPService.updateImageInfo(game.getHeaderFile(),"game");
        for(MultipartFile file : game.getScreenFile()){
            String screanshots = googleGCPService.updateImageInfo(file,"game");
            screenshotsList.add(screanshots);
        }
        adminService.gameInsert(game,headerImage,screenshotsList);
        return "redirect:/admin/adminForm";
    }


}
