package kr.game.sale.repository.game.review.vote;

import kr.game.sale.entity.game.review.vote.ReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Long> ,ReviewVoteCustom {

}
