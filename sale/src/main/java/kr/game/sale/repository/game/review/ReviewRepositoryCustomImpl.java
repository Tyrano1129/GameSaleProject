package kr.game.sale.repository.game.review;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static kr.game.sale.entity.game.review.QReview.review;
import static kr.game.sale.entity.game.QGame.game;
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> searchReview(ReviewPageDTO reviewPageDTO, Pageable pageable) {

        QueryResults<Review> queryResults = queryFactory
                .selectFrom(review)
                .where(
                        isEqualToGameId(Long.valueOf(reviewPageDTO.getAppId()))
                        /*containsKeyword(game.name, gameSearchDTO.getSearchKeyword()),
                        containsKeyword(game.name, gameSearchDTO.getInnerSearchKeyword()),
                        containsCategory(game.genres, gameSearchDTO.getSearchCategory()),
                        containsPublisher(game.publisher, gameSearchDTO.getSearchPublisher()),
                        koreanSupported(gameSearchDTO.getSearchInterfaceKorean())*/
                )
                /*.orderBy(orderSpecifier)*/
                .offset(pageable.getOffset()) // 페이징 시작 위치 설정
                .limit(pageable.getPageSize()) // 한 페이지에 보여줄 아이템 수 설정
                .fetchResults();

        long totalCount = queryResults.getTotal();
        List<Review> reviews = queryResults.getResults();//*

        return new PageImpl<>(reviews,pageable,totalCount);
    }

    private BooleanExpression isEqualToGameId(Long value) {
        return Objects.isNull(value) ? null : review.game.steamAppid.eq(value);
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
