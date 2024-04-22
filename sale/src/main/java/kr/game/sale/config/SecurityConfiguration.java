package kr.game.sale.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomAuthSuccessHandler customAuthSuccessHandler;

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
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/admin/**").hasAnyRole("MANAGER", "ADMIN") // 관리자만 접근가능
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form
                                .loginPage("/users/loginForm") // 우리가 만든 로그인폼으로 인터셉트됩니다.
                                .loginProcessingUrl("/userLogin")
                                .failureHandler(customAuthFailureHandler()) // 로그인실패시 할 작업
                                .successHandler(customAuthSuccessHandler)
                )
                .oauth2Login(Customizer.withDefaults()
                )
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling(exceptions ->
                        exceptions
//                                .accessDeniedHandler(accessDeniedHandler())
                                .accessDeniedPage("/")
                );
        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }
}
