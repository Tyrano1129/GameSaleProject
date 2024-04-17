package kr.game.sale.entity.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGame is a Querydsl query type for Game
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGame extends EntityPathBase<Game> {

    private static final long serialVersionUID = 1607034613L;

    public static final QGame game = new QGame("game");

    public final StringPath detailedDescription = createString("detailedDescription");

    public final StringPath developers = createString("developers");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final StringPath enName = createString("enName");

    public final StringPath genres = createString("genres");

    public final StringPath headerImage = createString("headerImage");

    public final StringPath minRequirements = createString("minRequirements");

    public final StringPath movies = createString("movies");

    public final StringPath name = createString("name");

    public final StringPath platform = createString("platform");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath publisher = createString("publisher");

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final StringPath rcmRequirements = createString("rcmRequirements");

    public final DateTimePath<java.util.Date> releaseDate = createDateTime("releaseDate", java.util.Date.class);

    public final StringPath screenshots = createString("screenshots");

    public final NumberPath<Long> steamAppid = createNumber("steamAppid", Long.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final StringPath supportedLanguages = createString("supportedLanguages");

    public QGame(String variable) {
        super(Game.class, forVariable(variable));
    }

    public QGame(Path<? extends Game> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGame(PathMetadata metadata) {
        super(Game.class, metadata);
    }

}

