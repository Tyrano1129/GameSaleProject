package kr.game.sale.entity.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    REG_DATE("regDate"),
    DISCOUNT("discount"),
    POPULARITY("popularity"),
    HIGH_PRICE("highPrice"),
    LOW_PRICE("lowPrice");
    private final String type;

}
