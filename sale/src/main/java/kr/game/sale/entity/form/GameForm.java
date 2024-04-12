package kr.game.sale.entity.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameForm {
    private Long id;
    private String name;
    private String supportedLanguages;
    private int price;
    private String viewprice;
    private String developers;
    private String releaseDate;
    private String genres;
    private String minRequirements;
    private String rcmRequirements;
    private String headerImage;
    private String screenshots;

    @Builder
    public GameForm(Long id, String name, String supportedLanguages, int price, String viewprice, String developers, String releaseDate, String genres, String minRequirements, String rcmRequirements, String headerImage, String screenshots) {
        this.id = id;
        this.name = name;
        this.supportedLanguages = supportedLanguages;
        this.price = price;
        this.viewprice = viewprice;
        this.developers = developers;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.minRequirements = minRequirements;
        this.rcmRequirements = rcmRequirements;
        this.headerImage = headerImage;
        this.screenshots = screenshots;
    }
}
