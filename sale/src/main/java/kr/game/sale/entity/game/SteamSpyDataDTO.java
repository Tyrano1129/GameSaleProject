package kr.game.sale.entity.game;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamSpyDataDTO {
    private int appid;
    private String name;
    private int price;
    private String genre;
    private String publisher;
}
