package com.beamofsoul.bip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRolePermission is a Querydsl query type for RolePermission
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRolePermission extends EntityPathBase<RolePermission> {

    private static final long serialVersionUID = 548878499L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRolePermission rolePermission = new QRolePermission("rolePermission");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final QPermission permission;

    public final QRole role;

    public QRolePermission(String variable) {
        this(RolePermission.class, forVariable(variable), INITS);
    }

    public QRolePermission(Path<? extends RolePermission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRolePermission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRolePermission(PathMetadata metadata, PathInits inits) {
        this(RolePermission.class, metadata, inits);
    }

    public QRolePermission(Class<? extends RolePermission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.permission = inits.isInitialized("permission") ? new QPermission(forProperty("permission")) : null;
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
    }

}

