package kr.game.sale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${steamspyAPI.baseUrl}") // application.properties에 정의한 baseUrl
    private String firstServiceBaseUrl;

    @Value("${steamAPI.baseUrl}") // application.properties에 정의한 baseUrl
    private String secondServiceBaseUrl;

    @Bean(name="steamspyAPI")
    public WebClient steamspyAPIWebClient(WebClient.Builder builder) {
        return builder.baseUrl(firstServiceBaseUrl).build();
    }

    @Bean(name="steamAPI")
    public WebClient steamAPIWebClient(WebClient.Builder builder) {
        return builder.baseUrl(secondServiceBaseUrl).build();
    }

}