package kr.game.sale.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "001"; // 존재하지 않는 아이디일 때.
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "002"; // 내부 시스템 문제로 로그인 요청을 처리할 수 없을 때.
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "003"; // 아이디나 비밀번호가 일치하지 않을 때.
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "004"; // 인증 요청이 거부되었을 때.
        } else {
            errorMessage = exception.getMessage();
            errorMessage = errorMessage + "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        }
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        this.setDefaultFailureUrl("/users/loginForm?error=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
