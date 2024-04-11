package kr.game.sale.entity.user;

import lombok.Data;

@Data
public class CartView {
    private String img;
    private String name;
    private int price;
    private int discount;
    private int total;
}
