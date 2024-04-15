package kr.game.sale.entity.game.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewSortType {
    REG_DATE,
    POSITIVE,
    NEGATIVE,
    HLEPFUL;

}
