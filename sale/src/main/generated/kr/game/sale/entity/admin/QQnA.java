package kr.game.sale.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnA is a Querydsl query type for QnA
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnA extends EntityPathBase<QnA> {

    private static final long serialVersionUID = 673185004L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnA qnA = new QQnA("qnA");

    public final StringPath fileName = createString("fileName");

    public final DateTimePath<java.time.LocalDateTime> localDateTime = createDateTime("localDateTime", java.time.LocalDateTime.class);

    public final StringPath qnaAnwerContent = createString("qnaAnwerContent");

    public final StringPath qnaContent = createString("qnaContent");

    public final NumberPath<Long> qnaId = createNumber("qnaId", Long.class);

    public final BooleanPath qnaIsAnswered = createBoolean("qnaIsAnswered");

    public final StringPath qnaRespondent = createString("qnaRespondent");

    public final StringPath qnaTitle = createString("qnaTitle");

    public final kr.game.sale.entity.user.QUsers user;

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
        this.user = inits.isInitialized("user") ? new kr.game.sale.entity.user.QUsers(forProperty("user")) : null;
    }

}

