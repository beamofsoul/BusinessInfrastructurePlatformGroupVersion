package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.bip.entity.SensitiveWord;
import com.querydsl.core.types.Path;


/**
 * QSensitiveWord is a Querydsl query type for SensitiveWord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSensitiveWord extends EntityPathBase<SensitiveWord> {

    private static final long serialVersionUID = 223702338L;

    public static final QSensitiveWord sensitiveWord = new QSensitiveWord("sensitiveWord");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    public final BooleanPath available = createBoolean("available");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final BooleanPath regular = createBoolean("regular");

    public final StringPath replacement = createString("replacement");

    public final StringPath word = createString("word");

    public QSensitiveWord(String variable) {
        super(SensitiveWord.class, forVariable(variable));
    }

    public QSensitiveWord(Path<? extends SensitiveWord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSensitiveWord(PathMetadata metadata) {
        super(SensitiveWord.class, metadata);
    }

}

