package kr.game.sale.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticePageing {
    private int startNum;
    private int endNum;
    private String title;
}
