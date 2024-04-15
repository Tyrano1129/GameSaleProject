package kr.game.sale.service;

import kr.game.sale.entity.user.Wishlist;
import kr.game.sale.entity.user.WishlistView;
import kr.game.sale.repository.user.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<WishlistView> getAllWishlistView() {
        List<Wishlist> wishlist = wishlistRepository.findByUsers(userService.getLoggedInUser());

        List<WishlistView> wishlistViewList = new ArrayList<>();
        for (Wishlist wishlistItem : wishlist) {
            WishlistView wishlistView = new WishlistView();
            wishlistView.setWishlistId(wishlistView.getWishlistId());
            wishlistView.setName(wishlistItem.getGame().getName());
            int price = wishlistItem.getGame().getPrice();
            wishlistView.setPrice(price);
            int discount = wishlistItem.getGame().getDiscount();
            wishlistView.setDiscount(discount);
            wishlistView.setTotal(getDiscountedPrice(price, discount));
            wishlistViewList.add(wishlistView);
        }
        return wishlistViewList;
    }

    // 할인율이 적용된 가격을 얻는 메서드입니다
    private int getDiscountedPrice(int price, int discount) {
        double discountedPrice = price * (1.0 - discount / 100.0);
        int total = (int) discountedPrice;
        total = total - (total % 10); // 1의 자리를 버림
        return total;
    }
}
