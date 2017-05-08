package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.bip.entity.Permission;
import com.querydsl.core.types.Path;


/**
 * QPermission is a Querydsl query type for Permission
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPermission extends EntityPathBase<Permission> {

    private static final long serialVersionUID = -1166662445L;

    public static final QPermission permission = new QPermission("permission");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    public final StringPath action = createString("action");

    public final BooleanPath available = createBoolean("available");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final StringPath group = createString("group");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath resourceType = createString("resourceType");

    public final NumberPath<Long> sort = createNumber("sort", Long.class);

    public final StringPath url = createString("url");

    public QPermission(String variable) {
        super(Permission.class, forVariable(variable));
    }

    public QPermission(Path<? extends Permission> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPermission(PathMetadata metadata) {
        super(Permission.class, metadata);
    }

}

