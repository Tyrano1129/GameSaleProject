package kr.game.sale.entity.user;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import lombok.Data;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;
    @ManyToOne
    @JoinColumn(name="steam_appid")
    private Game game;
}
