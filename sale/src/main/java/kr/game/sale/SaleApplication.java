package kr.game.sale;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    // 스프링 빈 컨테이너에 우리가 직접 등록
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }
    // 회원가입시 비밀번호 암호화 하는 빈
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    // 애플리케이션이 실행될 때 3개의 테스트계정이 DB에 생성됩니다.
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
                manager.setUserNickname("매니저");
                user.setUserNickname("일반유저");

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
