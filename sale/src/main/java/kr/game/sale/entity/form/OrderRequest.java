package kr.game.sale.entity.form;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<String> selectedItems;
}
