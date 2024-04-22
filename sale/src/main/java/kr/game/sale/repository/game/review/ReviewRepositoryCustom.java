package kr.game.sale.repository.game.review;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.SortType;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewPageDTO;
import kr.game.sale.entity.game.review.ReviewResponse;
import kr.game.sale.entity.game.review.report.ReviewReportDTO;
import kr.game.sale.entity.game.review.vote.ReviewVote;
import kr.game.sale.entity.game.review.vote.ReviewVoteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {
    public Page<Review> searchReview(ReviewPageDTO gameSearchDTO, Pageable pageable);

    public Page<ReviewResponse> findReviewsAndVoteByUserId(ReviewPageDTO reviewPageDTO, Pageable pageable);

    public Review findReviewByUserId(Long userId, Long steamAppId);

    public long addVote(ReviewVoteDTO reviewVoteDTO);

    public long decreaseVote(Long id);

    public int findVoteCntById(Long id);

    public long reportReview(ReviewReportDTO reviewReportDTO);

    public List<Review> findAllReportedReviews();
    Page<Review> searchAdminReviews(Pageable pageable);

}
