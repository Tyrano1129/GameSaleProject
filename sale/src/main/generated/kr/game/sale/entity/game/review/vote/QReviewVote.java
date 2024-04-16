package kr.game.sale.entity.game.review.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewVote is a Querydsl query type for ReviewVote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewVote extends EntityPathBase<ReviewVote> {

    private static final long serialVersionUID = 253943187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewVote reviewVote = new QReviewVote("reviewVote");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.game.sale.entity.game.review.QReview review;

    public final kr.game.sale.entity.user.QUsers users;

    public QReviewVote(String variable) {
        this(ReviewVote.class, forVariable(variable), INITS);
    }

    public QReviewVote(Path<? extends ReviewVote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewVote(PathMetadata metadata, PathInits inits) {
        this(ReviewVote.class, metadata, inits);
    }

    public QReviewVote(Class<? extends ReviewVote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new kr.game.sale.entity.game.review.QReview(forProperty("review"), inits.get("review")) : null;
        this.users = inits.isInitialized("users") ? new kr.game.sale.entity.user.QUsers(forProperty("users")) : null;
    }

}

