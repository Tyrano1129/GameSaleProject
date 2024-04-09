package kr.game.sale.config.auth;

import kr.game.sale.entity.user.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private Users users;
    private Map<String, Object> attributes;

    PrincipalDetails(Users users) { // 일반 로그인 객체
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        this.users = users;
    }

    // OAuth2.0 로그인시 사용
    public PrincipalDetails(Users users, Map<String, Object> attributes) {
        this.attributes = attributes; // 구글 로그인할때 프로필 정보 이메일이 넘겨옴
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> users.getUserRole().toString());
        return collection;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttribute(String name) {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
