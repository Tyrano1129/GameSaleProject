package kr.game.sale.config.auth;

import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// /login 자동 UserDetailsService 타입으로 IoC loadUserByUserName();
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users usersEntity = userRepository.findByUsername(username).isEmpty()? null : userRepository.findByUsername(username).get();
        if(usersEntity != null)
        System.out.println(" 유저 디테일 객체 생성 !!! " + usersEntity);
        return new PrincipalDetails(usersEntity);
    }
}
