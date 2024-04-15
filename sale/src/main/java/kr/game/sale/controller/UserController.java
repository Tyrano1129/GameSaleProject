package kr.game.sale.controller;

import kr.game.sale.entity.user.Users;
import kr.game.sale.service.QnAService;
import kr.game.sale.service.UserService;
import kr.game.sale.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final QnAService qnaService;
    private final WishlistService wishlistService;

    @GetMapping("/joinForm")
    public String userJoinForm() {
        return "/users/userJoinForm";
    }

    @PostMapping("/join")
    public String userJoin(Users users) {
        userService.addUser(users);
        return "redirect:/";
    }

    @PostMapping("/emailDuplicated")
    @ResponseBody
    public String emailDuplicated(@RequestParam("username") String username) {
        log.info("username : {}", username);
        boolean exist = userService.isEmailExist(username);
        return exist ? "invalid" : "valid";
    }

    @PostMapping("/sendCodeToEmail")
    @ResponseBody
    public String emailTest(@RequestParam("email") String email) {
        return userService.verifyWithEmail(email);
    }

    @GetMapping("/payment")
    public String paymentForm() {
        return "users/payment";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "users/userLoginForm";
    }


    @PostMapping("/checkLog")
    @ResponseBody
    public String checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("현재 로그인 중인 유저 : {}", authentication.getName());
        if (!authentication.getName().equals("anonymousUser")) {
            // 사용자가 인증되어 있는 경우
            return "valid";
        } else {
            // 사용자가 인증되어 있지 않은 경우
            return "invalid";
        }
    }

    @GetMapping("/myPage")
    public String myPage() {
        return "users/myPage";
    }

    @GetMapping("/userQuestion")
    public String userQuestion(Model model) {
        model.addAttribute("qnaList", qnaService.findAllByUsers());
        return "users/userQuestion";
    }

    @GetMapping("/userWishlist")
    public String userWishlist(Model model) {
        model.addAttribute("wishlistViewList", wishlistService.getAllWishlistView());
        return "users/userWishlist";
    }

    @GetMapping("/userUpdate")
    public String userUpdate(Model model) {
        userService.getLoggedInUser();
        model.addAttribute("users", userService.getLoggedInUser());
        log.info("user : {}", userService.getLoggedInUser());
        return "users/userUpdate";
    }

    @PostMapping("/doUpdate")
    public String doUpdate(Users usersForm) {
        userService.updateUser(usersForm);
        return "redirect:/users/userUpdate";
    }

    @GetMapping("/userResign")
    public String userResign(Model model) {
        model.addAttribute("users", userService.getLoggedInUser());
        return "users/userResign";
    }

    @PostMapping("/doResign")
    public String doResign() {
        userService.userResign();
        return "redirect:/";
    }
}
