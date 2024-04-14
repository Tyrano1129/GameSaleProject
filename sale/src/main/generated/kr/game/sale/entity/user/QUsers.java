package kr.game.sale.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = -743674754L;

    public static final QUsers users = new QUsers("users");

    public final ListPath<Cart, QCart> carts = this.<Cart, QCart>createList("carts", Cart.class, QCart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

    public final ListPath<kr.game.sale.entity.admin.QnA, kr.game.sale.entity.admin.QQnA> qnas = this.<kr.game.sale.entity.admin.QnA, kr.game.sale.entity.admin.QQnA>createList("qnas", kr.game.sale.entity.admin.QnA.class, kr.game.sale.entity.admin.QQnA.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final StringPath userNickname = createString("userNickname");

    public final StringPath userPhone = createString("userPhone");

    public final EnumPath<UserRole> userRole = createEnum("userRole", UserRole.class);

    public QUsers(String variable) {
        super(Users.class, forVariable(variable));
    }

    public QUsers(Path<? extends Users> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsers(PathMetadata metadata) {
        super(Users.class, metadata);
    }

}

