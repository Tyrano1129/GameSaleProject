package kr.game.sale.controller;

import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.form.PaymentView;
import kr.game.sale.entity.form.WishRequest;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.CartService;
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

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final QnAService qnaService;
    private final WishlistService wishlistService;
    private final CartService cartService;

    @GetMapping("/joinForm")
    public String userJoinForm() {
        return "users/userJoinForm";
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
        return exist ? "valid" : "invalid";
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
    public String myPage(Model model) {
        Users user = userService.getLoggedInUser();
        List<Payment> paymentList = userService.findAllByUser(user);
        if (paymentList.isEmpty())
            return "users/myPage";
        // order 담기
        List<String> ordernum = userService.orderNumList(paymentList);
        List<PaymentView> list = userService.paymentViewList(paymentList, ordernum);
        log.info("ordernum = {}", ordernum);
        log.info("paymentList = {}", paymentList);
        log.info("list = {}", list);
        model.addAttribute("list", list);
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
        return "redirect:/users/logout";
    }

    @PostMapping("/addToWishlist")
    @ResponseBody
    public String addToWishlist(@RequestParam String appId) {
        if (wishlistService.getMySingleWish(appId) != null)
            return "fail";
        wishlistService.addToWishlist(appId);
        return "success";
    }

    @PostMapping("/deleteWishlist")
    @ResponseBody
    public String deleteWishlist(@RequestBody List<String> wishNumbers) {
        wishlistService.deleteWishlistByIdList(wishNumbers);
        return "success";
    }

    @GetMapping("/deleteWish")
    public String deleteItem(@RequestParam Long wishlistId) {
        // wishlistId를 이용하여 해당 아이템을 삭제하는 로직을 구현
        // 예를 들어, 서비스나 리포지토리를 이용하여 아이템을 삭제할 수 있습니다.
        // 여기서는 단순히 wishlistId를 받아서 해당 아이템을 삭제하는 것으로 가정합니다.
        log.info("위시리스트 아이디 : {}", wishlistId);
        wishlistService.deleteAWish(wishlistId);
        // 삭제 로직을 수행한 후, 삭제 후에 사용자를 리다이렉트할 페이지를 반환합니다.
        return "redirect:/users/userWishlist";
    }

    @PostMapping("/moveToCart")
    public String move(@ModelAttribute WishRequest selectedItems,
                       @ModelAttribute WishRequest gameCodes) {
        List<String> wishIdList = selectedItems.getSelectedItems();
        List<String> gameIdList = selectedItems.getGameCodes();
        wishlistService.deleteWishlistByIdList(wishIdList);
        cartService.moveToCart(gameIdList);
        return "redirect:/cart/myCart";
    }

    @GetMapping("/userQuestionForm")
    public String goToQuestionForm() {
        return "users/userQuestionForm";
    }
}
