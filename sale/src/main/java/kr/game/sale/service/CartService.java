package kr.game.sale.service;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
import kr.game.sale.entity.user.Wishlist;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.user.CartRepository;
import kr.game.sale.repository.user.UserRepository;
import kr.game.sale.repository.user.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final WishlistRepository wishlistRepository;

    private static @NotNull CartView getCartView(Cart cart) {
        DecimalFormat format = new DecimalFormat("#,###");
        CartView view = new CartView();
        view.setCartId(cart.getId());
        view.setImg(cart.getGame().getHeaderImage());
        view.setName(cart.getGame().getName());
        view.setPrice(cart.getGame().getPrice());
        view.setPriceview(cart.getGame().getFormattedPrice());
        view.setDiscount(cart.getGame().getDiscount());
        view.setGameId(cart.getGame().getSteamAppid());
        int discount = cart.getGame().getDiscount();
        double discountedPrice = cart.getGame().getPrice() * (1.0 - discount / 100.0);
        int total = (int) discountedPrice;
        total = total - (total % 10); // 1의 자리를 버림
        // 할인된 금액
        int disprice = cart.getGame().getPrice() - total;
        view.setDisprice(disprice);
        log.info("disprice={}", disprice);
        // 할인된금액 - game 금액
        view.setTotal(total);
        view.setTotalview(format.format(total));
        view.setDispriceview(format.format(disprice));
        return view;
    }

    public List<CartView> getMyCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Users users = getOneUser(username);
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

    // order cart 값
    public List<CartView> getMyOrder(Users user, List<String> orderRequests) {
        List<CartView> cartList = new ArrayList<>();
        for (String list : orderRequests) {
            // 해당하는 카트값 하나씩 가지고오기
            Optional<Cart> cart = cartRepository.findByIdAndUsers(Long.parseLong(list), user);
            CartView view = null;
            // 값 있으면 true
            if (cart.isPresent()) {
                view = getCartView(cart.get());
            }
            cartList.add(view);
        }
        return cartList;
    }

    @Transactional
    public void deleteCartByIdList(List<String> stringList) {
        List<Long> longList = new ArrayList<>();
        for (String string : stringList) {
            longList.add(Long.parseLong(string));
        }
        cartRepository.deleteAllByIdInBatch(longList);
    }

    private Users getOneUser(String username) {
        return userRepository.findByUsername(username).isEmpty() ? null : userRepository.findByUsername(username).get();
    }

    @Transactional
    public void addCart(String steamAppid) {
        Users users = userService.getLoggedInUser();

        Cart cart = new Cart();
        cart.setUsers(users);
        Game game = gameRepository.findBySteamAppid(Integer.parseInt(steamAppid));
        cart.setGame(game);

        cartRepository.save(cart);
    }

    @Transactional
    public void moveToCart(List<String> wishIdStringList) {
        Users users = userService.getLoggedInUser();
        List<Long> wishIdLongList = new ArrayList<>();

        for (String string : wishIdStringList) {
            wishIdLongList.add(Long.parseLong(string));
        }

        List<Optional<Wishlist>> wishlistList = new ArrayList<>();
        for (Long id : wishIdLongList) {
            wishlistList.add(wishlistRepository.findById(id));
        }

        for (Optional<Wishlist> optionalWishlist : wishlistList) {
            if (optionalWishlist.isPresent()) {
                Wishlist wishlist = optionalWishlist.get();
                Game game = wishlist.getGame();

                Cart cart = new Cart();
                cart.setUsers(users);
//                Game game = gameRepository.findBySteamAppid(Integer.parseInt(steamAppid));
                cart.setGame(game);

                cartRepository.save(cart);
            }
        }
    }
}
