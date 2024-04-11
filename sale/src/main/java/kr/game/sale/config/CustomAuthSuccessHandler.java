package kr.game.sale.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.game.sale.config.auth.PrincipalDetails;
import kr.game.sale.entity.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

//        // authentication 에서 유저 객체 추출하기
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        Users users = principalDetails.getUsers();
//
//        // HttpSession 유저 객체 저장하기
//        HttpSession session = request.getSession();
//        session.setAttribute("users", users);
//
//        // Log
//        log.info("User : {}", users);

        response.sendRedirect("/");
    }
}
