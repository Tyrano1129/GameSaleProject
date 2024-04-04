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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        String myMsg = null;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 일치하지 않습니다.";
            myMsg = "비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = exception.getMessage();
            errorMessage = errorMessage + "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        }
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        this.setDefaultFailureUrl("/users/loginForm?error=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
