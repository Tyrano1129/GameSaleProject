package kr.game.sale.controller;

import kr.game.sale.entity.user.User;
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
    public String userJoin(User user) {
        userService.addUser(user);
        return "redirect:/users/joinForm";
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
        String code = userService.verifyWithEmail(email);
        return code;
    }

    @GetMapping("/payment")
    public String paymentForm(){
        return "users/payment";
    }
}
