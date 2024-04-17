package kr.game.sale.entity.user;

import lombok.Data;

@Data
public class WishlistView {
    private Long wishlistId;
    private Long steamAppid;
    private String name;
    private int price;
    private int discount;
    private int total;
}
