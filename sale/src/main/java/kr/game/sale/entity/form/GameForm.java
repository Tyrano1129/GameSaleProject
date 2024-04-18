package kr.game.sale.entity.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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
    private int stock;
    private String platform;
    private List<String> screenshots;
    private MultipartFile headerFile;
    private List<MultipartFile> screenFile;

    @Builder
    public GameForm(Long id, String name, String supportedLanguages, int price, String viewprice, String developers, String releaseDate, String genres, String minRequirements, String rcmRequirements, String headerImage, List<String> screenshots,int stock, String platform) {
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
        this.stock = stock;
        this.platform = platform;
    }
}
