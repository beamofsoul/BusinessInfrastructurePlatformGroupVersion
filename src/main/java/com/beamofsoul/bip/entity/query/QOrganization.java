package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.bip.entity.Organization;
import com.querydsl.core.types.Path;


/**
 * QOrganization is a Querydsl query type for Organization
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrganization extends EntityPathBase<Organization> {

    private static final long serialVersionUID = 1700918897L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrganization organization = new QOrganization("organization");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    public final BooleanPath available = createBoolean("available");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final StringPath descirption = createString("descirption");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final QOrganization parent;

//    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);
    
    public final NumberPath<Integer> sort = createNumber("sort", Integer.class);

    public QOrganization(String variable) {
        this(Organization.class, forVariable(variable), INITS);
//    	  super(Organization.class, forVariable(variable));
    }

    public QOrganization(Path<? extends Organization> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
//    	super(path.getType(), path.getMetadata());
    }

    public QOrganization(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
//    	 super(Organization.class, metadata);
    }

    public QOrganization(PathMetadata metadata, PathInits inits) {
        this(Organization.class, metadata, inits);
    }

    public QOrganization(Class<? extends Organization> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QOrganization(forProperty("parent"), inits.get("parent")) : null;
    }

}

