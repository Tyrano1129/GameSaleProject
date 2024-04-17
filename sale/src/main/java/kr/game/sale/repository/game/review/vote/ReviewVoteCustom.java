package kr.game.sale.repository.game.review.vote;

import kr.game.sale.entity.game.review.vote.ReviewVote;

public interface ReviewVoteCustom {

    public ReviewVote findReviewVoteByUserId(Long userId,Long reviewId);
}
