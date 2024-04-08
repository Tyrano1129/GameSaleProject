package kr.game.sale.entity.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Game {

    @Id
    @Column(name ="steam_appid")
    private int steamAppid;

    private String name;

    @Column(name ="detailed_description", columnDefinition="TEXT")
    private String detailedDescription;

    @Column(name ="supported_languages" ,columnDefinition = "TEXT CHARACTER SET UTF8")
    private String supportedLanguages;
    //화면에 뿌려줄 때 가공 메서드 필요

    @Lob
    @Column(name ="header_image")
    private String headerImage;

    @Column(name ="min_requirements",columnDefinition="TEXT")
    private String minRequirements;

    @Column(name ="rcm_requirements",columnDefinition="TEXT")
    private String rcmRequirements;

    private int price;

    private String developers;

    @Column(columnDefinition="TEXT")
    private String screenshots;

    @Column(columnDefinition="TEXT")
    private String movies;

    @Column(name ="release_date")
    private Date releaseDate;

    private String genres; //JSON 형태 저장


    public Game convertRawData(SteamGameDTO rawData){
        Game game = new Game();
        try{
            game.steamAppid = rawData.getSteamAppid();
            game.name = rawData.getName();
            game.supportedLanguages = rawData.getSupportedLanguages();
            game.detailedDescription = rawData.getDetailedDescription();
            String word = "apps";
            int srcStart = rawData.getHeaderImage().lastIndexOf(word);
            game.headerImage = rawData.getHeaderImage().substring(srcStart+word.length());
            game.minRequirements = rawData.getPcRequirements().getMinimum();
            game.rcmRequirements= rawData.getPcRequirements().getRecommended();
            int rawPrice = rawData.getPriceOverview().getInitial();
            if(rawData.getPriceOverview().getCurrency().equals("USD")){
                rawPrice*=10;
            }
            game.price = rawPrice; //달러/ 원화 바꿔서 처리
            game.developers = rawData.getDevelopers().get(0);
            try {
                game.screenshots =convertScreenshots(rawData);
                game.movies = convertMovies(rawData);
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException error => {}", e.getMessage());
            } catch (NullPointerException e) {
                log.error("NullPointerException error => {}", e.getMessage());
            }

            try {
                game.releaseDate = convertStringToDate(rawData.getReleaseDate().getDate());
            } catch (ParseException e) {
                log.error("ParseException error => {}", e.getMessage());
            }

            try {
                game.genres = convertGenres(rawData.getGenres());
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException => {}", e.getMessage());
            }
        }catch (Exception e){
            log.error("Exception => {}", e.getMessage());
        }
        return game;
    }

    public String convertScreenshots(SteamGameDTO rawData) throws JsonProcessingException {
        List<String> sList = new ArrayList<>();
        int startT = 0;
        int startFT = 0;
        for(Screenshot s : rawData.getScreenshots()){
            startT = s.getPathThumbnail().lastIndexOf("apps");
            startFT = s.getPathFull().lastIndexOf("apps");

            sList.add(s.getPathThumbnail().substring(startT+4));
            sList.add(s.getPathThumbnail().substring(startFT+4));
        }
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(sList);
    }

    public String convertMovies(SteamGameDTO rawData) throws JsonProcessingException {
        List<String> mList = new ArrayList<>();
        int startT = 0;
        int startM = 0;
        for(Movie m : rawData.getMovies()){
            startT = m.getThumbnail().lastIndexOf("apps");
            startM = m.getWebm().getMax().lastIndexOf("apps");

            mList.add(m.getThumbnail().substring(startT+4));
            mList.add(m.getWebm().getMax().substring(startM+4));
        }
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(mList);
    }

    public Date convertStringToDate(String rawDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        return formatter.parse(rawDate);
    }

    public String convertGenres(List<Genre> rawData) throws JsonProcessingException {

        List<String> data = new ArrayList<>();

        for(Genre g :rawData){
            data.add(g.getDescription());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }

    @Override
    public String toString() {
        return "Game{" +
                "steamAppid=" + steamAppid +
                "name=" + name + '\n' +
                "detailedDescription=" + detailedDescription + '\n' +
                "supportedLanguages=" + supportedLanguages + '\n' +
                "headerImage=" + headerImage + '\n' +
                "minRequirements=" + minRequirements + '\n' +
                "rcmRequirements=" + rcmRequirements + '\n' +
                "price=" + price + '\n' +
                "developers=" + developers + '\n' +
                "screenshots=" + screenshots + '\n' +
                "movies=" + movies + '\n' +
                "releaseDate=" + releaseDate + '\n' +
                "genres=" + genres + '\'' +
                '}';
    }

}