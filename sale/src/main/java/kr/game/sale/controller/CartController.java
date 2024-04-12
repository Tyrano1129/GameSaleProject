package kr.game.sale.controller;

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
    public String addToCart(@RequestParam("user_id") String userId, @RequestParam("steam_appid") String steamAppId) {
        // 받은 요청을 처리하여 장바구니에 상품을 추가하는 로직을 여기에 구현합니다.

        // 장바구니에 상품을 추가한 결과를 클라이언트에 응답합니다.
        return "success"; // 예시로 성공적으로 처리되었다는 문자열을 반환합니다.
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
    public String order(@ModelAttribute List<CartView> list) {
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
