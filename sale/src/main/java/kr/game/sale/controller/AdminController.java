package kr.game.sale.controller;

import kr.game.sale.service.AdminService;
import kr.game.sale.service.GameService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    @GetMapping
    public String adminForm(Model model){

        return "admin/adminForm";
    }
}
