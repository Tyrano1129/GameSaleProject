package kr.game.sale.config.oauth;

import kr.game.sale.config.auth.PrincipalDetails;
import kr.game.sale.config.oauth.provider.GoogleUserInfo;
import kr.game.sale.config.oauth.provider.KakaoUserInfo;
import kr.game.sale.config.oauth.provider.NaverUserInfo;
import kr.game.sale.config.oauth.provider.OAuth2UserInfo;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // userQuest 는 구글에서 코드를 받아서 accessToken을 응답 받는객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("user request clientRegistration : " + userRequest.getClientRegistration());
        System.out.println("user reuset getAccessToken :" + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest); // google 회원 프로필 조회
        System.out.println("get Attribute : " + oAuth2User.getAttributes());
        // loadUser --> Authentication 객체 안에 들어간다
        return processOAuthUser(userRequest, oAuth2User);
    }

    private OAuth2User processOAuthUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("요청 실패");
        }
        Optional<Users> userOptional =
                userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Users users = new Users();
        if (userOptional.isEmpty()) {
            String email = oAuth2UserInfo.getEmail();
            if (userRepository.findByUsername(email).isPresent()) {
                users = userRepository.findByUsername(email).get();
            } else {
                users.setUsername(email);
                users.setUserNickname(oAuth2UserInfo.getName());
                users.setUserRole(UserRole.ROLE_USER);
                users.setProvider(oAuth2UserInfo.getProvider());
                users.setProviderId(oAuth2UserInfo.getProviderId());
                userRepository.save(users);
            }
        } else {
            users = userOptional.get();
        }
        return new PrincipalDetails(users, oAuth2User.getAttributes());
    }
}
