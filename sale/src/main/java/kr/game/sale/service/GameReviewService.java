package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.GameSearchDTO;
import kr.game.sale.entity.game.SortType;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.game.review.ReviewPageDTO;
import kr.game.sale.entity.game.review.ReviewResponse;
import kr.game.sale.entity.game.review.vote.ReviewVote;
import kr.game.sale.entity.game.review.vote.ReviewVoteDTO;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.game.review.ReviewRepository;
import kr.game.sale.repository.game.review.vote.ReviewVoteRepository;
import kr.game.sale.repository.user.UserRepository;
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
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ReviewVoteRepository voteRepository;

    public void saveReview(Review review){
        repository.save(review);
    }

    public void saveReviewVote(ReviewVoteDTO reviewVoteDTO){
        Users user = userRepository.getReferenceById(Long.valueOf(reviewVoteDTO.getUserId()));
        Review review = repository.getReferenceById(Long.valueOf(reviewVoteDTO.getReviewId()));
        voteRepository.save(ReviewVote.builder().users(user).review(review).build());

        repository.addVote(reviewVoteDTO);
    }

    public Page<ReviewResponse> getList(ReviewPageDTO reviewPageDTO, Pageable pageable){

        Page<Review> reviews = repository.searchReview(reviewPageDTO, pageable);
        Page<ReviewResponse>  reviewResponses = reviews.map(r -> new ReviewResponse(r));
        return reviewResponses;
    }
}
