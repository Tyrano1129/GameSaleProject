package kr.game.sale.controller;

import kr.game.sale.entity.user.CartTest;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

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
    public String order(@ModelAttribute("cart") List<CartTest> list) {
        log.info("list : {}", list);
//        List<CartView> newList = form.get
//        for (CartView l : list) {
//            if (l.getChecked().equals("true")) {
//                newList.add(l);
//            }
//        }
//        log.info("newList: {}", newList);
        return "redirect:/cart/myCart";
    }
}
