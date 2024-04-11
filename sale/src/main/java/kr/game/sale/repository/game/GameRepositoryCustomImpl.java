package kr.game.sale.repository.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.netty.util.internal.StringUtil;
import kr.game.sale.entity.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.game.sale.entity.game.QGame.game;
public class GameRepositoryCustomImpl  implements GameRepositoryCustom{

    private final WebClient steamspyAPIWebclient;
    private final WebClient steamAPIWebclient;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public GameRepositoryCustomImpl(@Qualifier("steamspyAPI") WebClient steamspyAPIWebclient, @Qualifier("steamAPI") WebClient steamAPIWebclient, JPAQueryFactory queryFactory){
        this.steamspyAPIWebclient = steamspyAPIWebclient;
        this.steamAPIWebclient = steamAPIWebclient;
        this.queryFactory = queryFactory;
    }

    public List<Game> saveGameData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //https://steamspy.com/api.php?request=top100in2weeks
        String result = steamspyAPIWebclient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("request", "top100in2weeks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("===============");
        System.out.println(result);
        Map<String, SteamSpyDataDTO> gameMap = mapper.readValue(result, new TypeReference<Map<String, SteamSpyDataDTO>>() {
        });
        List<SteamSpyDataDTO> list = List.copyOf(gameMap.values());
        list = list.stream().filter(g -> g.getPrice() > 0).collect(Collectors.toList());

        List<SteamSpyDataDTO> topTenList = list.subList(0, 9);


        List<Game> Games = new ArrayList<>();
        for (SteamSpyDataDTO game : topTenList) {

            System.out.println("===============  steamAPI  ===============");
            //https://store.steampowered.com/api/appdetails?appids=730&l=korea
            String steamRs = steamAPIWebclient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/appdetails")
                            .queryParam("appids", game.getAppid())
                            .queryParam("l", "korean")
                            .build())
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(String.class).log()
                    .block();
            System.out.println("데이터" + steamRs);

            JsonNode rootNode = mapper.readTree(steamRs);
            JsonNode innerDataNode = rootNode.get(String.valueOf(game.getAppid())).get("data");
            SteamGameDTO gameMap2 = mapper.readValue(innerDataNode.toString(), new TypeReference<SteamGameDTO>() {
            });
            // SteamGame gameMap2= mapper.readValue(innerDataNode.toString(),SteamGame.class);
            if (gameMap2.getPriceOverview() == null) {
                gameMap2.setNewPriceOverview(game.getPrice());
            }
            System.out.println(gameMap2);
            Game gameObj = new Game();
            gameObj = gameObj.convertRawData(gameMap2);
            System.out.println(gameObj);
            Games.add(gameObj);
        }
        return Games;
    }

    @Override
    public List<Game> findMainList(SortType type, GameSearchCondition condition) {
        OrderSpecifier orderSpecifier = createOrderSpecifier(type);
        return queryFactory
                .selectFrom(game)
                .where(
                        koreanSupported(condition.getLanguage())

                )
                .orderBy(orderSpecifier)
                .limit(12)
                .fetch();
    }

    private BooleanExpression koreanSupported(String lang){
        return StringUtils.hasText(lang) ? game.supportedLanguages.contains(lang) : null;
    }


    private OrderSpecifier createOrderSpecifier(SortType sortType) {
        return switch (sortType) {
            case DISCOUNT -> new OrderSpecifier<>(Order.DESC, game.discount);
            case POPULARITY -> new OrderSpecifier<>(Order.ASC, game.steamRank);
            default ->   new OrderSpecifier<>(Order.DESC, game.releaseDate);
        };
    }

}
