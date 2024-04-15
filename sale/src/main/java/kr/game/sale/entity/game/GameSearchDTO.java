package kr.game.sale.entity.game;

import lombok.*;
import org.springframework.util.StringUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameSearchDTO {
    private String innerSearchKeyword;
    private String searchKeyword;
    private String sort;
    private SortType sortType = SortType.REG_DATE;
    private String searchCategory;
    private String searchPublisher;
    private String searchInterfaceKorean;

    private final int pageSize = 10;
    private int currPage;
    private int endPage;
    private int totalPage;
    private int startPage;

    public void setPageCxt(int currPage,int total) {

        this.totalPage = total;
        this.currPage = currPage;
        this.endPage = (int)(Math.ceil(this.currPage/10.0)*10);
        this.startPage = this.endPage-9;
        if(this.endPage > this.totalPage){
            this.endPage = this.totalPage;
        }

    }

    public void setSearchInterfaceKorean(String searchInterfaceKorean) {
        this.searchInterfaceKorean =  StringUtils.hasText(searchInterfaceKorean)? searchInterfaceKorean.equals("false")? "" :"한국":"";
    }

    public void setSort(String sort) {
        this.sort = sort;
        setSortType(sort);
    }
    public void setSortType(String sort) {
        switch (sort){
            case "최신순":
                this.sortType = SortType.REG_DATE;
                break;
            case "할인율순":
                this.sortType = SortType.DISCOUNT;
                break;
            case "인기순":
                this.sortType = SortType.POPULARITY;
                break;
            case "높은가격순":
                this.sortType = SortType.HIGH_PRICE;
                break;
            case "낮은가격순":
                this.sortType = SortType.LOW_PRICE;
                break;
            default:
                this.sortType = SortType.REG_DATE;
                break;
        }
    }
}