package com.beamofsoul.bip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeRecord is a Querydsl query type for CodeRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCodeRecord extends EntityPathBase<CodeRecord> {

    private static final long serialVersionUID = 1731420508L;

    public static final QCodeRecord codeRecord = new QCodeRecord("codeRecord");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final DateTimePath<java.util.Date> expiredDate = createDateTime("expiredDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCodeRecord(String variable) {
        super(CodeRecord.class, forVariable(variable));
    }

    public QCodeRecord(Path<? extends CodeRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeRecord(PathMetadata metadata) {
        super(CodeRecord.class, metadata);
    }

}

