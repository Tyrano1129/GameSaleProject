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
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.SimpleFormatter;

@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    private Game init(){
        //gameForm 여러곳에 이용하기위한 객체 생성
        return new Game();
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
                .screenshots(game.getScreenshots())
                .build();

        model.addAttribute("game",games);
        return "admin/gameForm";
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
    @PostMapping("/gmaeInsert")
    public String gmaeInsert(){
        return "admin/adminForm";
    }
}
