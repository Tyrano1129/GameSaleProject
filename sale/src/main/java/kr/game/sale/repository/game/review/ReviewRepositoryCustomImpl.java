package kr.game.sale.repository.game.review;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.game.review.*;
import kr.game.sale.entity.game.review.report.ReviewReportDTO;
import kr.game.sale.entity.game.review.vote.ReviewVoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kr.game.sale.entity.game.review.QReview.review;
import static kr.game.sale.entity.game.review.vote.QReviewVote.reviewVote;
import static kr.game.sale.entity.game.QGame.game;
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;



    public Page<ReviewResponse> findReviewsAndVoteByUserId(ReviewPageDTO reviewPageDTO, Pageable pageable) {

        OrderSpecifier orderSpecifier = createOrderSpecifier(reviewPageDTO.getSortType());

        // Review와 ReviewLike를 조인하여 특정 사용자가 특정 게임에 대해 좋아요한 리뷰를 가져옵니다.
        QueryResults<ReviewResponse> queryResults = queryFactory
                .select(Projections.bean(ReviewResponse.class,
                        review.reviewId,
                        review.isPositive,
                        review.content,
                        review.regDate,
                        review.game.steamAppid,
                        review.voteCnt,
                        review.users.userNickname.as("userNickName"),
                        reviewVote.id.as("reviewVoteId")))
                .from(review)
                .leftJoin(reviewVote)
                .on(review.reviewId.eq(reviewVote.review.reviewId).and(reviewVote.users.id.eq(reviewPageDTO.getUserId())))
                .where(
                        isEqualToGameId(Long.valueOf(reviewPageDTO.getAppId())),
                        isPositive(reviewPageDTO.getSortType())
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 페이징 시작 위치 설정
                .limit(pageable.getPageSize()) // 한 페이지에 보여줄 아이템 수 설정
                .fetchResults();

        long totalCount = queryResults.getTotal();
        List<ReviewResponse> ReviewResponses = queryResults.getResults();//*

        return new PageImpl<>(ReviewResponses,pageable,totalCount);
    }

    @Override
    public Page<Review> searchReview(ReviewPageDTO reviewPageDTO, Pageable pageable) {

        OrderSpecifier orderSpecifier = createOrderSpecifier(reviewPageDTO.getSortType());
        QueryResults<Review> queryResults = queryFactory
                .selectFrom(review)
                .where(
                        isEqualToGameId(Long.valueOf(reviewPageDTO.getAppId())),
                        isPositive(reviewPageDTO.getSortType())
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 페이징 시작 위치 설정
                .limit(pageable.getPageSize()) // 한 페이지에 보여줄 아이템 수 설정
                .fetchResults();

        long totalCount = queryResults.getTotal();
        List<Review> reviews = queryResults.getResults();//*

        return new PageImpl<>(reviews,pageable,totalCount);
    }

    @Override
    public Review findReviewByUserId(Long userId, Long steamAppId) {
        return queryFactory.selectFrom(review)
                .where(
                        isEqualToFKId(userId,steamAppId)
                ).fetchOne();
    }

    @Transactional
    @Override
    public long addVote(ReviewVoteDTO reviewVoteDTO) {
      return  queryFactory.update(review)
                .set(review.voteCnt, review.voteCnt.add(1))
                .where(review.reviewId.eq(Long.valueOf(reviewVoteDTO.getReviewId()))).execute();
    }

    @Transactional
    @Override
    public long reportReview(ReviewReportDTO reviewReportDTO) {
      return  queryFactory.update(review)
                .set(review.isReported, true)
                .where(review.reviewId.eq(Long.valueOf(reviewReportDTO.getReviewId())))
                .execute();
    }

    @Override
    public List<Review> findAllReportedReviews() {

        return queryFactory.selectFrom(review)
                .where(review.isReported.isTrue())
                .fetch();
    }

    private BooleanExpression isEqualToFKId(Long userId,Long steamAppId) {

        return Objects.isNull(userId) || Objects.isNull(steamAppId)? null
                : review.users.id.eq(userId).and(review.game.steamAppid.eq(steamAppId));
    }

    private BooleanExpression isPositive(ReviewSortType sortType) {
        if(Objects.isNull(sortType)) return null;

        return Objects.isNull(sortType) ? null:
                sortType == ReviewSortType.POSITIVE ? review.isPositive.eq(true) :
                sortType == ReviewSortType.NEGATIVE ? review.isPositive.eq(false) : null;
    }
    private BooleanExpression isEqualToUserId(Long value) {
        return Objects.isNull(value) ? null : review.users.id.eq(value);
    }

    private BooleanExpression isEqualToGameId(Long value) {
        return Objects.isNull(value) ? null : review.game.steamAppid.eq(value);
    }

    private OrderSpecifier createOrderSpecifier(ReviewSortType sortType) {
        return switch (sortType) {
            case HLEPFUL -> new OrderSpecifier<>(Order.DESC, review.voteCnt);
            default ->   new OrderSpecifier<>(Order.DESC, review.regDate);
        };
    }


    /*public Page<Review> searchReview(ReviewPageDTO reviewPageDTO, Pageable pageable) {




        *//*OrderSpecifier orderSpecifier = createOrderSpecifier(gameSearchDTO.getSortType());

        QueryResults<Game> queryResults = queryFactory
                .selectFrom(game)
                .where(
                        containsKeyword(game.name, gameSearchDTO.getSearchKeyword()),
                        containsKeyword(game.name, gameSearchDTO.getInnerSearchKeyword()),
                        containsCategory(game.genres, gameSearchDTO.getSearchCategory()),
                        containsPublisher(game.publisher, gameSearchDTO.getSearchPublisher()),
                        koreanSupported(gameSearchDTO.getSearchInterfaceKorean())
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 페이징 시작 위치 설정
                .limit(pageable.getPageSize()) // 한 페이지에 보여줄 아이템 수 설정
                .fetchResults();

        long totalCount = queryResults.getTotal();
        List<Game> games = queryResults.getResults();*//*

        return new PageImpl<>(games,pageable,totalCount);
    }*/
}
