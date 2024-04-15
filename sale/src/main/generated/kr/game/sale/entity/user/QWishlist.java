package kr.game.sale.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishlist is a Querydsl query type for Wishlist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlist extends EntityPathBase<Wishlist> {

    private static final long serialVersionUID = -2076263537L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishlist wishlist = new QWishlist("wishlist");

    public final kr.game.sale.entity.game.QGame game;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUsers users;

    public QWishlist(String variable) {
        this(Wishlist.class, forVariable(variable), INITS);
    }

    public QWishlist(Path<? extends Wishlist> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishlist(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishlist(PathMetadata metadata, PathInits inits) {
        this(Wishlist.class, metadata, inits);
    }

    public QWishlist(Class<? extends Wishlist> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.game = inits.isInitialized("game") ? new kr.game.sale.entity.game.QGame(forProperty("game")) : null;
        this.users = inits.isInitialized("users") ? new QUsers(forProperty("users")) : null;
    }

}

