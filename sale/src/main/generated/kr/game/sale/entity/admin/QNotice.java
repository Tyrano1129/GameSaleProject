package kr.game.sale.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Expression;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = 1568808624L;

    public static ConstructorExpression<Notice> create(Expression<Long> noticeId, Expression<String> noticeTitle, Expression<String> noticeContent, Expression<Integer> noticeCount, Expression<String> noticeWriter, Expression<java.time.LocalDateTime> noticeDate) {
        return Projections.constructor(Notice.class, new Class<?>[]{long.class, String.class, String.class, int.class, String.class, java.time.LocalDateTime.class}, noticeId, noticeTitle, noticeContent, noticeCount, noticeWriter, noticeDate);
    }

    public static final QNotice notice = new QNotice("notice");

    public final StringPath noticeContent = createString("noticeContent");

    public final NumberPath<Integer> noticeCount = createNumber("noticeCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> noticeDate = createDateTime("noticeDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> noticeId = createNumber("noticeId", Long.class);

    public final StringPath noticeTitle = createString("noticeTitle");

    public final StringPath noticeWriter = createString("noticeWriter");

    public QNotice(String variable) {
        super(Notice.class, forVariable(variable));
    }

    public QNotice(Path<? extends Notice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotice(PathMetadata metadata) {
        super(Notice.class, metadata);
    }

}

