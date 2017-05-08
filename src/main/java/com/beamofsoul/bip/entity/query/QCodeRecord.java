package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.beamofsoul.bip.entity.CodeRecord;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QCodeRecord is a Querydsl query type for CodeRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCodeRecord extends EntityPathBase<CodeRecord> {

    private static final long serialVersionUID = -1613135966L;

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

