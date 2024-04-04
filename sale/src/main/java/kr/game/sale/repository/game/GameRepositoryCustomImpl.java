package kr.game.sale.repository.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.SteamGameDTO;
import kr.game.sale.entity.game.SteamSpyDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameRepositoryCustomImpl  implements GameRepositoryCustom{

    private final WebClient steamspyAPIWebclient;
    private final WebClient steamAPIWebclient;

    @Autowired
    public GameRepositoryCustomImpl(@Qualifier("steamspyAPI") WebClient steamspyAPIWebclient, @Qualifier("steamAPI") WebClient steamAPIWebclient){
        this.steamspyAPIWebclient = steamspyAPIWebclient;
        this.steamAPIWebclient = steamAPIWebclient;
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


/*
    public static void main(String[] args) throws JsonProcessingException {
        String url = "https://steamspy.com/api.php?request=top100in2weeks";
        System.out.println(url);
       // WebClient client = WebClient.builder().baseUrl("https://steamspy.com/api.php").
       //         defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

      *//*WebClient client2 = WebClient.create("https://steamspy.com/api.php");
        Mono<List<SteamSpyData>> result = client2.get()
               // .uri("/request/{request}","top100in2weeks")
                .uri("https://steamspy.com/api.php?request=top100in2weeks")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SteamSpyData.class)
               .collectList();
                 for(SteamSpyData data  : result) {
                     System.out.println(data.toString());
                 }*//*

//https://store.steampowered.com/api/appdetails?appids=730&l=korean
        ObjectMapper mapper = new ObjectMapper();
        WebClient client2 = WebClient.create("https://steamspy.com/api.php");
        String result = client2.get()
                .uri("https://steamspy.com/api.php?request=top100in2weeks")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("===============");
        System.out.println(result);
        Map<String, SteamSpyData> gameMap = mapper.readValue(result, new TypeReference<Map<String, SteamSpyData>>() {});
        List<SteamSpyData> list = List.copyOf(gameMap.values());

        List<SteamSpyData> topTenList = list.subList(0,9);
        for(SteamSpyData ket : topTenList){
            System.out.println(ket);
        }
        System.out.println("===============  steamAPI  ===============");
        WebClient steamAPI = WebClient.builder()
                .baseUrl("https://store.steampowered.com")
                .build();

        String steamRs = steamAPI.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/api/appdetails")
                                .queryParam("appids", topTenList.get(0).getAppid())
                                .queryParam("l","korean")
                                .build())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class).log()
                .block();
        System.out.println("데이터"+steamRs);

//https://store.steampowered.com/api/appdetails?appids=730&l=korean



       *//* ObjectMapper mapper = new ObjectMapper();
        JSONPObject json = new JSONPObject("JSON.parse", result);
        String jsonStr = mapper.writeValueAsString(json);
        System.out.println(jsonStr);
        Map<Integer, Object> m = mapper.readValue(jsonStr, Map.class);
        for(int ket : m.keySet()){
            System.out.println(ket);
        }*//*



    }*/
}
