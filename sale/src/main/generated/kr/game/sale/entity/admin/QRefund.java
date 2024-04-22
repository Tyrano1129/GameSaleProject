package kr.game.sale.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRefund is a Querydsl query type for Refund
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefund extends EntityPathBase<Refund> {

    private static final long serialVersionUID = 1673684816L;

    public static final QRefund refund = new QRefund("refund");

    public final StringPath paymentIds = createString("paymentIds");

    public final DateTimePath<java.time.LocalDateTime> refundAplctdate = createDateTime("refundAplctdate", java.time.LocalDateTime.class);

    public final NumberPath<Long> refundId = createNumber("refundId", Long.class);

    public final StringPath refundReason = createString("refundReason");

    public final StringPath refundViewDate = createString("refundViewDate");

    public final BooleanPath refundWhether = createBoolean("refundWhether");

    public QRefund(String variable) {
        super(Refund.class, forVariable(variable));
    }

    public QRefund(Path<? extends Refund> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRefund(PathMetadata metadata) {
        super(Refund.class, metadata);
    }

}

