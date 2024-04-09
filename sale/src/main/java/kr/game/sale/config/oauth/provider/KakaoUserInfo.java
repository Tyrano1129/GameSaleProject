package kr.game.sale.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes; // getAttributes
    private final Map<String, Object> attributesProperties; // getAttributes
    private final Map<String, Object> attributesAccount; // getAttributes
    private final Map<String, Object> attributesProfile; // getAttributes

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesProperties = (Map<String, Object>) attributes.get("properties");
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return (String) attributesProperties.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributesAccount.get("email");
    }
}
