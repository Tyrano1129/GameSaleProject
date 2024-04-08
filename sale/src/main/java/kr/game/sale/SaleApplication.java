package kr.game.sale;

import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class SaleApplication {
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SaleApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner initData() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Users admin = new Users();
                Users manager = new Users();
                Users user = new Users();

                admin.setUsername("admin");
                manager.setUsername("manager");
                user.setUsername("user");

                admin.setPassword(new BCryptPasswordEncoder().encode("1"));
                manager.setPassword(new BCryptPasswordEncoder().encode("1"));
                user.setPassword(new BCryptPasswordEncoder().encode("1"));

                admin.setUserNickname("관리자");
                admin.setUserNickname("매니저");
                admin.setUserNickname("일반유저");

                admin.setUserRole(UserRole.ROLE_ADMIN);
                manager.setUserRole(UserRole.ROLE_MANAGER);
                user.setUserRole(UserRole.ROLE_USER);

                userRepository.save(admin);
                userRepository.save(manager);
                userRepository.save(user);
            }
        };
    }
}
