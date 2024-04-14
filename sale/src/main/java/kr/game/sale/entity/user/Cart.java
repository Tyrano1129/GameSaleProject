package kr.game.sale.entity.user;

import jakarta.persistence.*;
import kr.game.sale.entity.game.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "steam_appid")
    private Game game;
}
