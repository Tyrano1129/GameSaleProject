package kr.game.sale.entity.game.review;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  reviewId;

    private boolean isPositive;

    private boolean isReported;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name ="vote_cnt")
    @ColumnDefault("0")
    private int voteCnt;

    @ManyToOne
    @JoinColumn(name="steam_appid" , referencedColumnName = "steam_appid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game game;

    @ManyToOne
    @JoinColumn(name="user_id" , referencedColumnName ="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users users;

    @Transient
    private String reviewDateView;

    public void setReviewDateView(String localDateTime){
        this.reviewDateView = localDateTime;
    }
    @Builder
    public Review( boolean isPositive, String content, LocalDateTime localDateTime, Users user,Game game) {
        this.isPositive = isPositive;
        this.content = content;
        this.regDate =  localDateTime;
        this.users = user;
        this.game = game;
    }

}
