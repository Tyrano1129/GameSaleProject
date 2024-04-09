package kr.game.sale.config.oauth.provider;

// 여러 OAuth2.0 제공자들이 있는데 속성값들이 다 다르다 => 공통적으로 만들어주는 작업 필요하다
public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getName();

    String getEmail();
}
