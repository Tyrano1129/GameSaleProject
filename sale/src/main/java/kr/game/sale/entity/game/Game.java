package kr.game.sale.entity.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import kr.game.sale.entity.game.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="steam_appid")
    private Long steamAppid;

    private String name;

    @Column(name ="en_name")
    private String enName;

    @Column(name ="detailed_description", columnDefinition="TEXT")
    private String detailedDescription;

    @Column(name ="supported_languages" ,columnDefinition = "TEXT CHARACTER SET UTF8")
    private String supportedLanguages;

    @Lob
    @Column(name ="header_image")
    private String headerImage;

    @Column(name ="min_requirements",columnDefinition="TEXT")
    private String minRequirements;

    @Column(name ="rcm_requirements",columnDefinition="TEXT")
    private String rcmRequirements;

    private int price;

    private String developers;

    private String publisher;

    @Column(columnDefinition="TEXT")
    private String screenshots;

    @Column(columnDefinition="TEXT")
    private String movies;

    @Column(name ="release_date")
    private Date releaseDate;

    private int discount;

    private String genres; //JSON 형태 저장

    private String platform;

    private int rating;
    private int stock = 5;


    @Transient
    private List<String> genreList;
    @Transient
    private List<String> movieList;
    @Transient
    private List<String> screenshootsList;
    @Transient
    private int salesPrice;
    @Transient
    private String gameDate;
    @Transient
    private String priceView;

    @Builder
    public Game(Long steamAppid, String name, String enName, String detailedDescription, String supportedLanguages, String headerImage, String minRequirements, String rcmRequirements, int price, String developers, String publisher, String screenshots, String movies, Date releaseDate, int discount, String genres, String platform, int rating, List<String> genreList, List<String> movieList, List<String> screenshootsList, int salesPrice,int stock) {
        this.steamAppid = steamAppid;
        this.name = name;
        this.enName = enName;
        this.detailedDescription = detailedDescription;
        this.supportedLanguages = supportedLanguages;
        this.headerImage = headerImage;
        this.minRequirements = minRequirements;
        this.rcmRequirements = rcmRequirements;
        this.price = price;
        this.developers = developers;
        this.publisher = publisher;
        this.screenshots = screenshots;
        this.movies = movies;
        this.releaseDate = releaseDate;
        this.discount = discount;
        this.genres = genres;
        this.platform = platform;
        this.rating = rating;
        this.genreList = genreList;
        this.movieList = movieList;
        this.screenshootsList = screenshootsList;
        this.salesPrice = salesPrice;
    }

    public boolean isKoreanSupported(){
        return this.supportedLanguages.contains("한국");
    }

    public String getFormattedPrice(){
       // int bNum = price/100;
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(price);
    }

    public int getSalesPrice() {
        if(this.salesPrice == 0){
            if(discount == 0){
                return price;
            }
            double discountedPrice = this.price * (1.0 - discount / 100.0);
            int total = (int) discountedPrice;
            this.salesPrice = total - (total % 10);
        }
        return salesPrice;
    }

    public String getFormattedSalesPrice(){
        if(this.salesPrice == 0){
            if(discount == 0){
                return getFormattedPrice();
            }
            double discountedPrice = this.price * (1.0 - discount / 100.0);
            int total = (int) discountedPrice;
            this.salesPrice = total - (total % 10);
        }
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(this.salesPrice);
    }

    public List<String> getScreenshootsList(){
        if(this.screenshootsList == null && this.screenshots != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.screenshootsList = mapper.readValue(this.screenshots, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("screenshootsList = {}",screenshootsList);
        return this.screenshootsList;
    }
    public List<String> getMovieList(){
        if(this.movieList == null && this.movies != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.movieList = mapper.readValue(this.movies, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return this.movieList;
    }

    public List<String> getGenreList(){

        if(this.genreList == null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.genreList = mapper.readValue(this.genres, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return this.genreList;
    }

    public Game convertRawData(SteamGameDTO rawData){
        Game game = new Game();
        try{
            //game.steamAppid = (long) rawData.getSteamAppid();
            game.platform= "steam";
            game.stock =5;
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
            }else{
                rawPrice/=100;
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
            } catch (Exception e) {
                log.error("Exception error => {}", e.getMessage());
            }

            try {
                game.genres = convertGenres(rawData.getGenres());
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException => {}", e.getMessage());
            }
            Random rd = new Random();
            game.discount = rd.nextInt(11)+5;
            game.rating = convertRating(rawData);

        }catch (Exception e){
            log.error("Exception => {}", e.getMessage());
        }
        return game;
    }

    public int convertRating(SteamGameDTO rawData){
        int rawRate =rawData.getRating();
        if(rawData.getRatings().getSteamGermany() != null){
            rawRate = Integer.parseInt(rawData.getRatings().getSteamGermany().getRequiredAge());
        }
        if(rawData.getRatings().getPegi() != null){
            rawRate = Integer.parseInt(rawData.getRatings().getPegi().getRating());
        }
        int rate = 19;
        if (rawRate < 17 ){
            rate = 15;
        }
        if(rawRate < 13){
            rate = 12;
        }
        if(rawRate < 10){
            rate = 0;
        }
        return rate;
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
    public String convertDateToString() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return this.releaseDate == null ? "" : formatter.format(this.releaseDate);
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
                "enName=" + enName + '\n' +
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
                "rating=" + rating + '\'' +
                '}';
    }
    public void stockDown(){
        this.stock -= 1;
    }

}