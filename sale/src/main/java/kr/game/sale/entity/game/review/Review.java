package kr.game.sale.entity.game.review;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Users;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "steamAppid")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY ,optional = false,cascade = CascadeType.REMOVE)
    @JoinColumn(name="steam_appid" , referencedColumnName = "steam_appid", nullable = false)
    private Game game;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY ,optional = false,cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id" , referencedColumnName ="user_id", nullable = false)
    private Users users;



    @Builder
    public Review( boolean isPositive, String content, LocalDateTime localDateTime, Users user,Game game) {
        this.isPositive = isPositive;
        this.content = content;
        this.regDate =  localDateTime;
        this.users = user;
        this.game = game;
    }

}
