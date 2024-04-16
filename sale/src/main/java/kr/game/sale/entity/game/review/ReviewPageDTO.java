package kr.game.sale.entity.game.review;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewPageDTO {

    private final int pageSize = 10;
    private int currPage;
    private int endPage;
    private int totalPage;
    private int startPage;
    private String appId;
    private String sort;
    private long userId;
    private ReviewSortType sortType;

    public void setSort(String sort) {
        this.sort = sort;
        this.sortType = ReviewSortType.valueOf(sort);
    }

    public void setPageCxt(int currPage, int total) {
        this.totalPage = total;
        this.currPage = currPage;
        this.endPage = (int)(Math.ceil(this.currPage/10.0)*10);
        this.startPage = this.endPage-9;
        if(this.endPage > this.totalPage){
            this.endPage = this.totalPage;
        }

    }

}
