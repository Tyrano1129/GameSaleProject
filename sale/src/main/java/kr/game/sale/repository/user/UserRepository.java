package kr.game.sale.repository.user;

import kr.game.sale.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    // 외부 유저 검색
    Optional<Users> findByProviderAndProviderId(String provider, String providerId);
}
