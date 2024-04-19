package kr.game.sale.entity.game.review.vote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ReviewVoteDTO {
    private String reviewId;
    private long userId;

}
