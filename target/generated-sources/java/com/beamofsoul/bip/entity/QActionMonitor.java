package com.beamofsoul.bip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActionMonitor is a Querydsl query type for ActionMonitor
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QActionMonitor extends EntityPathBase<ActionMonitor> {

    private static final long serialVersionUID = 1626582214L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActionMonitor actionMonitor = new QActionMonitor("actionMonitor");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final StringPath effect = createString("effect");

    public final NumberPath<Integer> hazardLevel = createNumber("hazardLevel", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ipAddress = createString("ipAddress");

    public final StringPath macAddress = createString("macAddress");

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final StringPath specificAction = createString("specificAction");

    public final StringPath target = createString("target");

    public final QUser user;

    public QActionMonitor(String variable) {
        this(ActionMonitor.class, forVariable(variable), INITS);
    }

    public QActionMonitor(Path<? extends ActionMonitor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActionMonitor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActionMonitor(PathMetadata metadata, PathInits inits) {
        this(ActionMonitor.class, metadata, inits);
    }

    public QActionMonitor(Class<? extends ActionMonitor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

