package kr.game.sale.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public String userForm(){
        return "users/userForm";
    }

    @GetMapping("/payment")
    public String paymentForm(){
        return "users/payment";
    }
}
