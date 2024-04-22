package kr.game.sale;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.CartRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.TimeZone;

@SpringBootApplication
@RequiredArgsConstructor
public class SaleApplication {
    private final UserRepository userRepository;
//    private final CartRepository cartRepository;
    @PostConstruct
    public void init() {
        // timezone 설정 JAVA 서버에 올라갔을때 위한 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    public static void main(String[] args) {
        SpringApplication.run(SaleApplication.class, args);
    }

    // 스프링 빈 컨테이너에 우리가 직접 등록
//    @Bean
//    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
//        return new JPAQueryFactory(em);
//    }

    // 회원가입시 비밀번호 암호화 하는 빈
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    // 애플리케이션이 실행될 때 테스트 계정들이 DB에 생성됩니다.
    @Bean
    public ApplicationRunner initData() {
        return args -> {
            Users users = userRepository.findByUsername("admin").isEmpty()? null : userRepository.findByUsername("admin").get();
            if (users != null)
                return;

            Users admin = new Users(); // 관리자
            Users manager = new Users(); // 매니저

            admin.setUsername("admin");
            manager.setUsername("manager");

            admin.setPassword(new BCryptPasswordEncoder().encode("1"));
            manager.setPassword(new BCryptPasswordEncoder().encode("1"));

            admin.setUserNickname("관리자");
            manager.setUserNickname("매니저");

            admin.setUserPhone("010-1234-1234");
            manager.setUserPhone("010-1234-1234");

            admin.setUserRole(UserRole.ROLE_ADMIN);
            manager.setUserRole(UserRole.ROLE_MANAGER);

            userRepository.save(admin);
            userRepository.save(manager);

            // 일반 유저 10명
            for (int i = 1; i <= 10; i += 1) {
                Users user = new Users();
                user.setUsername("test" + i);
                user.setPassword(new BCryptPasswordEncoder().encode("1"));
                user.setUserNickname("테스트" + i);
                user.setUserPhone("010-1234-1234");
                user.setUserRole(UserRole.ROLE_USER);
                userRepository.save(user);
            }
        };
    }
}
