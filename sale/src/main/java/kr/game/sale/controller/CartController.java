package kr.game.sale.controller;

import kr.game.sale.entity.form.OrderRequest;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
import kr.game.sale.entity.form.CartListForm;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.CartTest;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.CartViewListDTO;
import kr.game.sale.service.CartService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public String order(@ModelAttribute OrderRequest orderRequest) {
        List<String> list = orderRequest.getSelectedItems();
        log.info("cart 의 id들 : {}", list.toString());

       // 현재 로그인 중인 유저
        Users users = userService.getLoggedInUser();

        return "redirect:/cart/myCart";
    }
}
