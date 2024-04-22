package kr.game.sale.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnA is a Querydsl query type for QnA
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnA extends EntityPathBase<QnA> {

    private static final long serialVersionUID = 673185004L;

    public static ConstructorExpression<QnA> create(Expression<Long> qnaId, Expression<? extends kr.game.sale.entity.user.Users> users, Expression<String> qnaTitle, Expression<String> qnaContent, Expression<java.time.LocalDateTime> localDateTime, Expression<Boolean> qnaIsAnswered, Expression<String> qnaRespondent, Expression<String> qnaAnwerContent) {
        return Projections.constructor(QnA.class, new Class<?>[]{long.class, kr.game.sale.entity.user.Users.class, String.class, String.class, java.time.LocalDateTime.class, boolean.class, String.class, String.class}, qnaId, users, qnaTitle, qnaContent, localDateTime, qnaIsAnswered, qnaRespondent, qnaAnwerContent);
    }

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnA qnA = new QQnA("qnA");

    public final DateTimePath<java.time.LocalDateTime> localDateTime = createDateTime("localDateTime", java.time.LocalDateTime.class);

    public final StringPath qnaAnwerContent = createString("qnaAnwerContent");

    public final StringPath qnaContent = createString("qnaContent");

    public final NumberPath<Long> qnaId = createNumber("qnaId", Long.class);

    public final BooleanPath qnaIsAnswered = createBoolean("qnaIsAnswered");

    public final StringPath qnaRespondent = createString("qnaRespondent");

    public final StringPath qnaTitle = createString("qnaTitle");

    public final kr.game.sale.entity.user.QUsers users;

    public QQnA(String variable) {
        this(QnA.class, forVariable(variable), INITS);
    }

    public QQnA(Path<? extends QnA> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnA(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnA(PathMetadata metadata, PathInits inits) {
        this(QnA.class, metadata, inits);
    }

    public QQnA(Class<? extends QnA> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.users = inits.isInitialized("users") ? new kr.game.sale.entity.user.QUsers(forProperty("users")) : null;
    }

}

