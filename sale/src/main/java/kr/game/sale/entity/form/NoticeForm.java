package kr.game.sale.entity.form;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NoticeForm {
    private String title;
    private String content;
    private String writer;
}
