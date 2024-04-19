package kr.game.sale.repository.game.review.vote;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.game.review.vote.ReviewVote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static kr.game.sale.entity.game.review.QReview.review;
import static kr.game.sale.entity.game.review.vote.QReviewVote.reviewVote;
@RequiredArgsConstructor
@Slf4j
public class ReviewVoteCustomImpl implements ReviewVoteCustom{


    private final JPAQueryFactory queryFactory;
    @Override
    public ReviewVote findReviewVoteByUserId(Long userId,Long reviewId) {
        return queryFactory.selectFrom(reviewVote)
                .where(
                        isEqualToId(userId, reviewId)
                ).fetchOne();
    }
    private BooleanExpression isEqualToId(Long userId,Long reviewId) {

        return Objects.isNull(userId) || Objects.isNull(reviewId)? null
                : reviewVote.users.id.eq(userId).and(reviewVote.review.reviewId.eq(reviewId));
    }
}
