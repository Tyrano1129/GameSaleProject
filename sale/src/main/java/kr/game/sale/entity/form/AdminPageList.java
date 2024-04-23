package kr.game.sale.entity.form;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class AdminPageList<T> {
    private List<T> contnet;
    private int start;
    private int end;
    private int page;
    private int total;

    public void setPageCxt(int currPage, int total){
        this.total = total;
        this.page = currPage;

        if (this.page <= 5 || this.total <= 5) {
            this.end = Math.min(5, total);
            if(this.total <= 5) {
                this.start = 0;
            }else{
                this.start = this.end - 5;
            }
        } else {
            this.end = ((int)(Math.ceil(this.page / 5.0))) * 5;
            this.start = this.end - 5;
        }
        if(this.page >= this.end){
            this.start = this.end-5 + 5;
            this.end = ((int)(Math.ceil(this.page / 5.0))) * 5 + 5;
        }
        if (this.end > this.total) {
            this.end = this.total;
        }

        log.info("start = {}",this.start);
    }
}
