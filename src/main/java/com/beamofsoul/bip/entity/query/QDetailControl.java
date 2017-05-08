package com.beamofsoul.bip.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.beamofsoul.bip.entity.DetailControl;
import com.querydsl.core.types.Path;


/**
 * QDetailControl is a Querydsl query type for DetailControl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDetailControl extends EntityPathBase<DetailControl> {

    private static final long serialVersionUID = -1969542584L;

    public static final QDetailControl detailControl = new QDetailControl("detailControl");

    public final QBaseAbstractEntity _super = new QBaseAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    public final BooleanPath enabled = createBoolean("enabled");

    public final StringPath entityClass = createString("entityClass");

    public final StringPath filterRules = createString("filterRules");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> modifyDate = _super.modifyDate;

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public final StringPath unavailableColumns = createString("unavailableColumns");

    public QDetailControl(String variable) {
        super(DetailControl.class, forVariable(variable));
    }

    public QDetailControl(Path<? extends DetailControl> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDetailControl(PathMetadata metadata) {
        super(DetailControl.class, metadata);
    }

}

