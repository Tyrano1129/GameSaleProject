package kr.game.sale.repository.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.user.UserRole;
import kr.game.sale.entity.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.game.sale.entity.admin.QNotice.notice;
import static kr.game.sale.entity.user.QUsers.users;

@RequiredArgsConstructor
@Slf4j
public class UserCusRepositoryImpl implements UserCusRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Users> searchUser(Pageable pageable) {
        List<Users> content = queryFactory
                .select(users)
                .from(users)
                .where(users.userRole.ne(UserRole.ROLE_ADMIN))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Users> countQuery = queryFactory
                .select(users)
                .from(users);

        countQuery.fetch();

        return PageableExecutionUtils.getPage(content,pageable,() -> countQuery.fetchCount());
    }

}
