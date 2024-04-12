package kr.game.sale.service;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.CartRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    public final CartRepository cartRepository;
    public final UserRepository userRepository;

    public List<CartView> getMyCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Users users = userRepository.findByUsername(username);
        log.info("username : {} users : {}", username, users);
        List<Cart> myCartList = cartRepository.findAllByUsers(users);
        log.info("myCartList : {}", myCartList.toString());
        List<CartView> result = new ArrayList<>();
        if (myCartList.isEmpty())
            return result;
        for (Cart cart : myCartList) {

            Game game1 = new Game();
            game1.setHeaderImage("/1938090/header.jpg?t=1712591572");
            game1.setName("콜 오브 듀티®");
            game1.setPrice(69990);
            game1.setDiscount(15);

            cart.setGame(game1);

            CartView view = getCartView(cart);
            result.add(view);
        }
        return result;
    }

    private static @NotNull CartView getCartView(Cart cart) {
        CartView view = new CartView();
        view.setImg(cart.getGame().getHeaderImage());
        view.setName(cart.getGame().getName());
        view.setPrice(cart.getGame().getPrice());
        view.setDiscount(cart.getGame().getDiscount());
        int discount = cart.getGame().getDiscount();
        double discountedPrice = cart.getGame().getPrice() * (1.0 - discount / 100.0);
        int total = (int) discountedPrice;
        total = total - (total % 10); // 1의 자리를 버림
        view.setTotal(total);
        return view;
    }
}
