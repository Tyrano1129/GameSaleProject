package kr.game.sale.service;

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
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.game.review.ReviewRepository;
import kr.game.sale.repository.game.review.vote.ReviewVoteRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameReviewService {

    private final ReviewRepository repository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ReviewVoteRepository voteRepository;

    public List<Review> findAllReportedReviews(){return repository.findAllReportedReviews();}

    public void deleteReview(Long reiviewId){repository.deleteById(reiviewId);}

    public ReviewVote findReviewVoteByUserId(Long userId,Long reviewId){return voteRepository.findReviewVoteByUserId( userId,reviewId);}
    public Review findReviewByUserId(Long userId, Long steamAppId){return repository.findReviewByUserId(userId, steamAppId);}
    public String saveReview(Review review){
        try {
            repository.save(review);
            // 저장이 성공한 경우
        } catch (DataIntegrityViolationException e) {
            // 저장이 실패한 경우
            // 예외 처리
            e.printStackTrace(); // 예외를 적절히 처리하거나 로깅
            return "fail";
        }
        return "success";
    }

    public String saveReviewReport(ReviewReportDTO reviewReportDTO){
        return  repository.reportReview(reviewReportDTO)  > 0 ? "success" : "fail";
    }

    public String saveReviewVote(ReviewVoteDTO reviewVoteDTO){
        Users user = userRepository.getReferenceById(reviewVoteDTO.getUserId());
        Review review = repository.getReferenceById(Long.valueOf(reviewVoteDTO.getReviewId()));
        voteRepository.save(ReviewVote.builder().users(user).review(review).build());

      return  repository.addVote(reviewVoteDTO)  > 0 ? "success" : "fail";
    }

    public Page<ReviewResponse> getList(ReviewPageDTO reviewPageDTO, Pageable pageable){

        Page<Review> reviews = repository.searchReview(reviewPageDTO, pageable);
        Page<ReviewResponse>  reviewResponses = reviews.map(r -> new ReviewResponse(r));
        return reviewResponses;
    }

    public Page<ReviewResponse> findReviewsAndVoteByUserId(ReviewPageDTO reviewPageDTO, Pageable pageable){
        return repository.findReviewsAndVoteByUserId(reviewPageDTO, pageable);
    }

    public Page<Review> reviewsListPage(Pageable pageable){
        return repository.searchAdminReviews(pageable);
    }
}
