package kr.game.sale.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
                web.ignoring().requestMatchers("/favicon.ico", "/resources/**", "/error");
    }

    @Bean
    AuthenticationFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                        .requestMatchers("/user/**").authenticated() // 로그인 상태에서만 접근 가능
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // 매니저 이상만 접근가능
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN") // 관리자만 접근가능
                        .anyRequest().permitAll()
        ).formLogin(
                form ->
                        form.loginPage("/users/loginForm") // 우리가 만든 로그인폼으로 인터셉트됩니다.
                                .loginProcessingUrl("/userLogin")
                                .failureHandler(customAuthFailureHandler())
                                .defaultSuccessUrl("/", true) // 로그인에 성공하면 돌아올 페이지

        ).logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));
        return http.build();
    }
}
