package kr.game.sale.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Expression;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = -743674754L;

    public static ConstructorExpression<Users> create(Expression<Long> id, Expression<String> username, Expression<String> password, Expression<String> userNickname, Expression<String> userPhone, Expression<UserRole> userRole, Expression<String> provider, Expression<String> providerId) {
        return Projections.constructor(Users.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, UserRole.class, String.class, String.class}, id, username, password, userNickname, userPhone, userRole, provider, providerId);
    }

    public static final QUsers users = new QUsers("users");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

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

