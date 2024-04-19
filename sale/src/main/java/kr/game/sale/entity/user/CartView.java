package kr.game.sale.entity.user;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class CartView{
    private Long cartId;
    private Long gameId;
    private String img;
    private String name;
    private int price;
    private int discount;
    private int total;
    private String checked;
    private int disprice;
    private String priceview;
    private String totalview;
    private String dispriceview;
    private String platform;
}
