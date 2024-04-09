package kr.game.sale.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class NoticeForm {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String date;
    private int count;
}
