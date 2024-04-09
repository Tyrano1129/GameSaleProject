package kr.game.sale.controller;

import kr.game.sale.entity.user.Users;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

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
}
