package kr.game.sale;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class gameTest {
    private final GameService gameService;

    @Autowired
    gameTest(GameService gameService) {
        this.gameService = gameService;
    }


    @Test
    void contextLoads() throws JsonProcessingException {
        gameService.initData();
    }
}
