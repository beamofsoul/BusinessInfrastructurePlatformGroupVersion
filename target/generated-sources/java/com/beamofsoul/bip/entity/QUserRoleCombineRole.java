package com.beamofsoul.bip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRoleCombineRole is a Querydsl query type for UserRoleCombineRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRoleCombineRole extends EntityPathBase<UserRoleCombineRole> {

    private static final long serialVersionUID = 1483910582L;

    public static final QUserRoleCombineRole userRoleCombineRole = new QUserRoleCombineRole("userRoleCombineRole");

    public final StringPath id = createString("id");

    public final StringPath nickname = createString("nickname");

    public final StringPath roleId = createString("roleId");

    public final StringPath roleName = createString("roleName");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath username = createString("username");

    public QUserRoleCombineRole(String variable) {
        super(UserRoleCombineRole.class, forVariable(variable));
    }

    public QUserRoleCombineRole(Path<? extends UserRoleCombineRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRoleCombineRole(PathMetadata metadata) {
        super(UserRoleCombineRole.class, metadata);
    }

}

