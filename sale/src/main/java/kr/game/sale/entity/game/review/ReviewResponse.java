package kr.game.sale.entity.game.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponse {

    private long  reviewId;
    private Boolean isPositive;
    private String content;
    private LocalDateTime regDate;
    private long steamAppid;
    private String userNickName;
    private int voteCnt;
    private String formattedRegDate;
    private long reviewVoteId;

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
        setFormattedRegDate(this.regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public ReviewResponse(Review review){
        this.reviewId = review.getReviewId();
        this.isPositive = review.isPositive();
        this.content = review.getContent();
        //this.regDate = review.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.regDate = review.getRegDate();
        this.steamAppid = review.getGame().getSteamAppid();
        this.userNickName = review.getUsers().getUserNickname();
        this.voteCnt = review.getVoteCnt();
    }
}