package kr.game.sale.repository.admin;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.admin.QQnA;
import kr.game.sale.entity.admin.QnA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.game.sale.entity.admin.QQnA.qnA;
@RequiredArgsConstructor
@Slf4j
public class QnACusRepositoryImpl implements QnACusRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<QnA> serachQnA(Pageable pageable) {

        List<QnA> content = queryFactory
                .select(new QQnA(qnA))
                .from(qnA)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<QnA> countQuery = queryFactory
                .select(qnA)
                .from(qnA);

        countQuery.fetch();
        return PageableExecutionUtils.getPage(content,pageable,()->countQuery.fetchCount());

    }
}
