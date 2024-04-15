package kr.game.sale.repository.game.review;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static kr.game.sale.entity.game.QGame.game;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {




}
