package kr.game.sale.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Setter
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {
        String deniedUrl = errorPage + "?exception=" + e.getMessage();
        httpServletResponse.sendRedirect(deniedUrl);
    }
}
