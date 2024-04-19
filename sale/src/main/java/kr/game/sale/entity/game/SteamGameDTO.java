package kr.game.sale.entity.game;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.text.DecimalFormat;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamGameDTO {
    @JsonProperty("steam_appid")
    private Long steamAppid;

    private String name;

    @JsonProperty("detailed_description")
    private String detailedDescription;

    @JsonProperty("supported_languages")
    private String supportedLanguages;

    @JsonProperty("header_image")
    private String headerImage;

    @JsonProperty("required_age")
    private int rating;

    @JsonProperty("pc_requirements")
    private Requirements pcRequirements;

    @JsonProperty("price_overview")
    private PriceData priceOverview;

    @JsonProperty("ratings")
    private Ratings ratings;

    private List<String> developers;

    private List<Screenshot> screenshots;

    private List<Movie> movies;

    @JsonProperty("release_date")
    private ReleaseDate releaseDate;

    private List<Genre> genres;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Steam App ID: ").append(steamAppid).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Detailed Description: ").append(detailedDescription).append("\n");
        sb.append("Supported Languages: ").append(supportedLanguages).append("\n");
        sb.append("Header Image: ").append(headerImage).append("\n");
        sb.append("PC Requirements: ").append(pcRequirements).append("\n");
        sb.append("Price Overview: ").append(priceOverview).append("\n");
        sb.append("Developers: ").append(developers).append("\n");
        sb.append("Screenshots: ").append(screenshots).append("\n");
        sb.append("Movies: ").append(movies).append("\n");
        sb.append("Release Date: ").append(releaseDate).append("\n");
        sb.append("Genres: ").append(genres).append("\n");
        if(ratings.getSteamGermany() != null)  sb.append("rating.steamGermany: ").append(ratings.getSteamGermany().getRequiredAge()).append("\n");
        if(ratings.getPegi() != null)  sb.append("rating.pegi: ").append(ratings.getPegi().getRating()).append("\n");
        return sb.toString();
    }

    public void setNewPriceOverview(int price) {
        PriceData data = new PriceData();
        data.setCurrency("USD");
        data.setInitial(price);
        DecimalFormat df = new DecimalFormat("#,###");
        String str = df.format((int)((price*0.01)*1000));
        data.setFinalFormatted("₩ "+str );
        this.priceOverview = data;
    }
}

@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Ratings{
    @JsonProperty("steam_germany")
    private SteamGermany steamGermany;

    @JsonProperty("pegi")
    private Pegi pegi;

}

@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class SteamGermany{
    @JsonProperty("required_age")
    private String requiredAge;
}
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Pegi{
    private String rating;
}

@ToString
@Getter
@Setter
@NoArgsConstructor
class Requirements {
    private String minimum;
    private String recommended;

    // Getters and Setters
}

@ToString
@Getter
@Setter
@NoArgsConstructor
class PriceData {
    private String currency;
    private int initial;
    @JsonProperty("final")
    private int _final;

    @JsonProperty("discount_percent")
    private int discountPercent;
    @JsonProperty("initial_formatted")
    private String initialFormatted;

    @JsonProperty("final_formatted")
    private String finalFormatted;


    // Getters and Setters
}
@ToString
@Getter
@Setter
@NoArgsConstructor
class Screenshot {
    private int id;
    @JsonProperty("path_thumbnail")
    private String pathThumbnail;
    @JsonProperty("path_full")
    private String pathFull;

    // Getters and Setters
}
@ToString
@Getter
@Setter
@NoArgsConstructor
class Movie {
    private int id;
    private String name;
    private String thumbnail;
    private VideoFiles webm;
    private VideoFiles mp4;
    private boolean highlight;

    // Getters and Setters
}
@ToString
@Getter
@Setter
@NoArgsConstructor
class VideoFiles {
    @JsonProperty("480")
    private String _480;
    private String max;

    // Getters and Setters
}
@ToString
@Getter
@Setter
@NoArgsConstructor
class ReleaseDate {
    @JsonProperty("coming_soon")
    private boolean comingSoon;
    private String date;

    // Getters and Setters
}
@ToString
@Getter
@Setter
@NoArgsConstructor
class Genre {
    private String id;
    private String description;

    // Getters and Setters
}
