package kr.game.sale.service;

import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.CartView;
import kr.game.sale.entity.user.Users;
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
    private final UserService userService;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final WishlistRepository wishlistRepository;
    private final GameService gameService;

    // 카트뷰로 전환해주는 메서드
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
        view.setPlatform(cart.getGame().getPlatform());
        return view;
    }

    // 내 장바구니를 모두 가져오는 메서드
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

    // 위시리스트를 비우는 메서드
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

    // 장바구니에 담는 메서드
    @Transactional
    public void addCart(String steamAppid) {
        Users users = userService.getLoggedInUser();
        Cart cart = new Cart();
        cart.setUsers(users);
        Game game = gameRepository.findById(Long.valueOf(steamAppid)).get();
        cart.setGame(game);

        cartRepository.save(cart);
    }

    // 위시리스트에서 장바구니로 이동시키는 메서드
    @Transactional
    public void moveToCart(List<String> gameIdStringList) {
        Users users = userService.getLoggedInUser();
        List<Long> gameIdLongList = new ArrayList<>();

        for (String stringId : gameIdStringList) {
            if (this.getMySingleCart(stringId) != null)
                continue; // 이미 장바구니에 담겨져있는 상품이면 건너뛰기
            gameIdLongList.add(Long.parseLong(stringId));
        }

        for (Long gameId : gameIdLongList) {
            Cart cart = new Cart();
            cart.setUsers(users);
            cart.setGame(gameRepository.findBySteamAppid(gameId));
            cartRepository.save(cart);
        }
    }

    // 장바구니담기를 눌렀을 때, 이미 담겨져있는 상품인지를 확인하는 메서드
    public Cart getMySingleCart(String steamAppid) {
        Users users = userService.getLoggedInUser();
        Game game = gameRepository.findBySteamAppid(Long.valueOf(steamAppid));
        return cartRepository.findByUsersAndGame(users, game);
    }
}
