package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.SortType;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewPageDTO;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.game.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameReviewService {

    private final ReviewRepository repository;

    public void saveReview(Review review){
        repository.save(review);
    }

    public Page<Review> getList(ReviewPageDTO reviewPageDTO, Pageable pageable){return repository.searchReview(reviewPageDTO, pageable);}
}
