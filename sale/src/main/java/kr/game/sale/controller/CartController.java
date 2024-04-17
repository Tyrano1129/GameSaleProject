package kr.game.sale.controller;

import kr.game.sale.entity.form.OrderRequest;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.CartService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cart")
@Controller
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(@RequestParam String appId) {
        if (cartService.getMySingleCart(appId) != null)
            return "fail";
        cartService.addCart(appId);
        return "success";
    }

    @GetMapping("/myCart")
    public String cart(Model model) {
        List<CartView> cartViewList = cartService.getMyCart();
        model.addAttribute("cartViewList", cartViewList);
        return "users/userCart";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody List<String> orderNumbers) {
        cartService.deleteCartByIdList(orderNumbers);
        return "success";
    }

    @PostMapping("/order")
    public String order(@ModelAttribute OrderRequest orderRequest, Model model) {
        log.info("cart 의 id들 : {}", orderRequest.toString());
        Users users = userService.getLoggedInUser();
        List<String> lists = orderRequest.getSelectedItems();
        // 해당하는 아이디 값 다시 담기
        List<CartView> orderList = cartService.getMyOrder(users, lists);
        int listTotal = 0;
        int listDisPrice = 0;
        int listGamePrice = 0;
        for (CartView list : orderList) {
            listTotal += list.getTotal();
            listDisPrice += list.getDisprice();
            listGamePrice += list.getPrice();
        }
        log.info("list = {}", orderList);
        log.info("listTotal = {}", listTotal);
        log.info("listDisPrice = {}", listDisPrice);
        log.info("listGamePrice = {}", listGamePrice);
        DecimalFormat format = new DecimalFormat("#,###");
        String listTotalView = format.format(listTotal);
        String listDisPriceView = format.format(listDisPrice);
        String listGamePriceView = format.format(listGamePrice);
        // 현재 로그인 중인 유저
        model.addAttribute("orderList", orderList);
        model.addAttribute("listTotal", listTotal);
        model.addAttribute("listDisPrice", listDisPrice);
        model.addAttribute("listTotalView", listTotalView);
        model.addAttribute("listDisPriceView", listDisPriceView);
        model.addAttribute("listGamePriceView", listGamePriceView);
        model.addAttribute("username", users.getUserNickname());

        return "users/payment";
    }
}
