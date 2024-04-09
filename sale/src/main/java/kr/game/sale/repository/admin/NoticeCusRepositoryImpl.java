package kr.game.sale.repository.admin;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.admin.QNotice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.game.sale.entity.admin.QNotice.*;

@RequiredArgsConstructor
@Slf4j
public class NoticeCusRepositoryImpl implements NoticeCusRepository{
    private final JPAQueryFactory queryFactory;
    private BooleanExpression titlelike(String title){return title == null? null : notice.noticeTitle.like("%"+title+"%");}
    @Override
    public Page<Notice> searchPageSimple(Pageable pageable, String title) {
        List<Notice> content = queryFactory
                .select(new QNotice(notice))
                .from(notice)
                .where(titlelike(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Notice> countQuery = queryFactory
                .select(notice)
                .from(notice);

        countQuery.fetch();
        return PageableExecutionUtils.getPage(content,pageable,()->countQuery.fetchCount());
    }
}
