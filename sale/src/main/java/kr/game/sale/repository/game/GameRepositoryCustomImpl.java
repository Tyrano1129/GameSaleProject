package kr.game.sale.repository.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.support.PageableExecutionUtils;
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

        Map<String, SteamSpyDataDTO> gameMap = mapper.readValue(result, new TypeReference<Map<String, SteamSpyDataDTO>>() {
        });
        List<SteamSpyDataDTO> list = List.copyOf(gameMap.values());
        list = list.stream().filter(g -> g.getPrice() > 0).collect(Collectors.toList());

        List<SteamSpyDataDTO> topTenList = list.subList(0, 50);
        
        List<Game> Games = new ArrayList<>();
        for (SteamSpyDataDTO game : topTenList) {

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

            JsonNode rootNode = mapper.readTree(steamRs);
            JsonNode innerDataNode = rootNode.get(String.valueOf(game.getAppid())).get("data");
            SteamGameDTO gameMap2 = mapper.readValue(innerDataNode.toString(), new TypeReference<SteamGameDTO>() {
            });
            if (gameMap2.getPriceOverview() == null) {
                gameMap2.setNewPriceOverview(game.getPrice());
            }
            Game gameObj = new Game();
            gameObj = gameObj.convertRawData(gameMap2);
            if(!gameObj.getName().trim().equals(game.getName().trim())){
                gameObj.setEnName(game.getName());
            }
            gameObj.setPublisher(game.getPublisher());
            Games.add(gameObj);
        }
        return Games;
    }

    /*헤더 search bar 검색*/
    @Override
    public Page<Game> searchGamesByKeyword(GameSearchDTO gameSearchDTO, Pageable pageable) {
        OrderSpecifier orderSpecifier = createOrderSpecifier(gameSearchDTO.getSortType());

        QueryResults<Game> queryResults = queryFactory
                .selectFrom(game)
                .where(
                        containsKeyword(game.name, gameSearchDTO.getSearchKeyword()),
                        containsKeyword(game.name, gameSearchDTO.getInnerSearchKeyword()),
                        containsCategory(game.genres, gameSearchDTO.getSearchCategory()),
                        containsPublisher(game.publisher, gameSearchDTO.getSearchPublisher()),
                        koreanSupported(gameSearchDTO.getSearchInterfaceKorean())
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 페이징 시작 위치 설정
                .limit(pageable.getPageSize()) // 한 페이지에 보여줄 아이템 수 설정
                .fetchResults();

        long totalCount = queryResults.getTotal();
        List<Game> games = queryResults.getResults();

        return new PageImpl<>(games,pageable,totalCount);
    }

    /*모든 publisher 리스트 반환 함수*/
    @Override
    public List<String> findAllPublishers() {
        return queryFactory.selectDistinct(game.publisher).from(game).fetch();
    }

    /*Main 화면 게임 리스트 반환 함수*/
    @Override
    public List<Game> findMainList(SortType type, GameSearchCondition condition) {
        OrderSpecifier orderSpecifier = createOrderSpecifier(type);
        return queryFactory
                .selectFrom(game)
                .where(
                        koreanSupported(condition.getLanguage()),
                        game.stock.ne(0)
                )
                .orderBy(orderSpecifier)
                .limit(12)
                .fetch();
    }
    private BooleanExpression containsPublisher(StringExpression expression, String publisher) {
        return StringUtils.hasText(publisher) ?  expression.contains(publisher) : null;
    }
    private BooleanExpression containsCategory(StringExpression expression, String category) {
        return StringUtils.hasText(category) ?  expression.contains(category) : null;
    }
    private BooleanExpression containsKeyword(StringExpression expression, String keyword) {
        return StringUtils.hasText(keyword) ?  expression.contains(keyword) : null;
    }
    private BooleanExpression koreanSupported(String lang){
        return StringUtils.hasText(lang) ? game.supportedLanguages.contains(lang) : null;
    }


    private OrderSpecifier createOrderSpecifier(SortType sortType) {
        return switch (sortType) {
            case DISCOUNT -> new OrderSpecifier<>(Order.DESC, game.discount);
            case POPULARITY -> new OrderSpecifier<>(Order.ASC, game.steamAppid);
            case HIGH_PRICE -> new OrderSpecifier<>(Order.DESC, game.price);
            case LOW_PRICE -> new OrderSpecifier<>(Order.ASC, game.price);
            default ->   new OrderSpecifier<>(Order.DESC, game.releaseDate);
        };
    }

    // 김진수 페이징위한 추가
    @Override
    public Page<Game> serchAdminGameList(Pageable pageable){
        List<Game> content = queryFactory
                .select(game)
                .from(game)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Game> countQuery = queryFactory
                .select(game)
                .from(game);

        countQuery.fetch();
        return PageableExecutionUtils.getPage(content,pageable,()->countQuery.fetchCount());
    }
}
