package kr.game.sale.repository.admin;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.admin.QQnA;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.game.sale.entity.admin.QRefund.refund;
@RequiredArgsConstructor
@Slf4j
public class RefundCusRepositoryImpl implements RefundCusRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Refund> serchRefund(Pageable pageable) {

        List<Refund> content = queryFactory
                .select(refund)
                .from(refund)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Refund> countQuery = queryFactory
                .select(refund)
                .from(refund);

        countQuery.fetch();
        return PageableExecutionUtils.getPage(content,pageable,()->countQuery.fetchCount());

    }
}
