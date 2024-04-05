package kr.game.sale.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_USER("user"), ROLE_MANAGER("manager"), ROLE_ADMIN("admin");
    private final String role;
}
