package kr.game.sale.entity.form;

import lombok.Data;

import java.util.List;

@Data
public class AdminPageList<T> {
    private List<T> contnet;
    private int start;
    private int end;
    private int page;
    private int total;
}
