package kr.game.sale.service;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.game.GameRepository;
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
    public final GameRepository gameRepository;

    private static @NotNull CartView getCartView(Cart cart) {
        CartView view = new CartView();
        view.setCartId(cart.getId());
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

    public List<CartView> getMyCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Users users = userRepository.findByUsername(username);
        List<Cart> myCartList = cartRepository.findAllByUsers(users);
        log.info("myCartList : {}", myCartList.toString());
        List<CartView> result = new ArrayList<>();
        if (myCartList.isEmpty())
            return result;
        for (Cart cart : myCartList) {
            CartView view = getCartView(cart);
            result.add(view);
        }
        return result;
    }

    @Transactional
    public void deleteCartByIdList(List<String> stringList) {
        List<Long> longList = new ArrayList<>();
        for (String string : stringList) {
            longList.add(Long.parseLong(string));
        }
        cartRepository.deleteAllByIdInBatch(longList);
    }

    @Transactional
    public void addCart(String steamAppid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Users users = userRepository.findByUsername(username);

        Cart cart = new Cart();
        cart.setUsers(users);
        Game game = gameRepository.findBySteamAppid(Integer.parseInt(steamAppid));
        cart.setGame(game);

        cartRepository.save(cart);
    }
}
