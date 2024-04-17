package kr.game.sale.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -1527601874L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final kr.game.sale.entity.game.QGame game;

    public final StringPath gameCode = createString("gameCode");

    public final StringPath gameName = createString("gameName");

    public final NumberPath<Integer> gamePrice = createNumber("gamePrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> paymenDate = createDateTime("paymenDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final StringPath paymentOrdernum = createString("paymentOrdernum");

    public final NumberPath<Integer> paymentPrice = createNumber("paymentPrice", Integer.class);

    public final StringPath paymentResult = createString("paymentResult");

    public final kr.game.sale.entity.user.QUsers user;

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.game = inits.isInitialized("game") ? new kr.game.sale.entity.game.QGame(forProperty("game")) : null;
        this.user = inits.isInitialized("user") ? new kr.game.sale.entity.user.QUsers(forProperty("user")) : null;
    }

}

