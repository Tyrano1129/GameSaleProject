package kr.game.sale.entity.user;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import lombok.Data;

@Data
@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "steam_appid")
    private Game game;
}
