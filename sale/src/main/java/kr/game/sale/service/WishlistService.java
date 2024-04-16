package kr.game.sale.service;

import kr.game.sale.entity.user.Wishlist;
import kr.game.sale.entity.user.WishlistView;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.user.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    private final GameRepository gameRepository;

    private static @NotNull List<WishlistView> getWishlistViews(List<Wishlist> wishlist) {
        List<WishlistView> wishlistViewList = new ArrayList<>();
        for (Wishlist wishlistItem : wishlist) {
            WishlistView wishlistView = new WishlistView();
            wishlistView.setWishlistId(wishlistItem.getId());
//            wishlistView.setName(wishlistItem.getGame().getName());
//            int price = wishlistItem.getGame().getPrice();
//            wishlistView.setPrice(price);
//            int discount = wishlistItem.getGame().getDiscount();
//            wishlistView.setDiscount(discount);
//            wishlistView.setTotal(getDiscountedPrice(price, discount));
            wishlistViewList.add(wishlistView);
        }
        return wishlistViewList;
    }

    @Transactional
    public List<WishlistView> getAllWishlistView() {

        // 테스트
        Wishlist wl1 = new Wishlist();
        Wishlist wl2 = new Wishlist();
        Wishlist wl3 = new Wishlist();
        Wishlist wl4 = new Wishlist();
        Wishlist wl5 = new Wishlist();
        wl1.setUsers(userService.getLoggedInUser());
        wl2.setUsers(userService.getLoggedInUser());
        wl3.setUsers(userService.getLoggedInUser());
        wl4.setUsers(userService.getLoggedInUser());
        wl5.setUsers(userService.getLoggedInUser());
        wishlistRepository.save(wl1);
        wishlistRepository.save(wl2);
        wishlistRepository.save(wl3);
        wishlistRepository.save(wl4);
        wishlistRepository.save(wl5);

        List<Wishlist> wishlist = wishlistRepository.findByUsers(userService.getLoggedInUser());
        List<WishlistView> wishlistViewList = getWishlistViews(wishlist);
        return wishlistViewList;
    }

    // 할인율이 적용된 가격을 얻는 메서드입니다.
    private int getDiscountedPrice(int price, int discount) {
        double discountedPrice = price * (1.0 - discount / 100.0);
        int total = (int) discountedPrice;
        total = total - (total % 10); // 1의 자리를 버림
        return total;
    }

    // 위시리스트에 추가하는 메서드
    @Transactional
    public void addToWishlist(String appId) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUsers(userService.getLoggedInUser());
        wishlist.setGame(gameRepository.findBySteamAppid(Integer.parseInt(appId)));
        wishlistRepository.save(wishlist);
    }

    // 선택된 위시리스트들을 삭제하는 메서드
    @Transactional
    public void deleteWishlistByIdList(List<String> stringList) {
        List<Long> longList = new ArrayList<>();
        for (String string : stringList) {
            longList.add(Long.parseLong(string));
        }
        wishlistRepository.deleteAllById(longList);
    }

    // 삭제하기버튼을 눌렀을 때 해당하는 위시를 삭제하는 메서드
    @Transactional
    public void deleteAWish(Long id) {
        wishlistRepository.deleteById(id);
    }
}
