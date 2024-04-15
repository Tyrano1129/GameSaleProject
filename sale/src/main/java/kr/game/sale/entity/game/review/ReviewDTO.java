package kr.game.sale.entity.game.review;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private Boolean isPositive;
    private String content;
    private String userId;
    private String appId;


}
