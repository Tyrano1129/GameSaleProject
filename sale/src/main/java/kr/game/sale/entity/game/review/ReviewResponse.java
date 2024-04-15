package kr.game.sale.entity.game.review;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ReviewResponse {

    private long  reviewId;
    private boolean isPositive;
    private String content;
    private String regDate;
    private long steamAppid;
    private String userNickName;

    public ReviewResponse(Review review){
        this.reviewId = review.getReviewId();
        this.isPositive = review.isPositive();
        this.content = review.getContent();
        this.regDate = review.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.steamAppid = review.getGame().getSteamAppid();
        this.userNickName = review.getUsers().getUserNickname();
    }
}