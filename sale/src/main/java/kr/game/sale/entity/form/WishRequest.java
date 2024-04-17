package kr.game.sale.entity.form;

import lombok.Data;

import java.util.List;

@Data
public class WishRequest {
    private List<String> selectedItems;
    private List<String> gameCodes;
}
