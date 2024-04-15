package kr.game.sale.entity.user;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class CartView implements Serializable {
    private Long cartId;
    private String img;
    private String name;
    private int price;
    private int discount;
    private int total;
    private String checked;
 
}
