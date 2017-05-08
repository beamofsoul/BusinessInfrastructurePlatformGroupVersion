package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.bip.entity.Role;
import com.beamofsoul.bip.entity.User;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -86502647L;

    public static final QUser user = new QUser("user");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath photo = createString("photo");

    public final SetPath<Role, QRole> roles = this.<Role, QRole>createSet("roles", Role.class, QRole.class, PathInits.DIRECT2);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}
