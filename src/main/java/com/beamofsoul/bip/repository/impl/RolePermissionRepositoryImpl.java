package com.beamofsoul.bip.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;

import com.beamofsoul.bip.entity.dto.RolePermissionDTO;
import com.beamofsoul.bip.entity.query.QRolePermission;
import com.beamofsoul.bip.repository.RolePermissionRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@NoRepositoryBean
public class RolePermissionRepositoryImpl<RolePermission> implements RolePermissionRepositoryCustom<RolePermission, Long> {
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<RolePermissionDTO> findAllRolePermissionMapping() {
		JPAQuery<RolePermission> query = new JPAQuery<RolePermission>(entityManager); 
		QRolePermission rolePermission = QRolePermission.rolePermission;
		List<RolePermissionDTO> rpDTOList = 
			query.select(Projections.constructor(RolePermissionDTO.class, 
					rolePermission.role.id,
					rolePermission.role.name,
					rolePermission.permission.id,
					rolePermission.permission.name,
					rolePermission.permission.available,
					rolePermission.permission.parentId,
					rolePermission.permission.resourceType,
					rolePermission.permission.url,
					rolePermission.permission.action,
					rolePermission.permission.sort))
				.from(rolePermission)
				.rightJoin(rolePermission.role)
				.leftJoin(rolePermission.permission)
				.orderBy(rolePermission.role.id.desc(),rolePermission.permission.id.asc())
				.fetch();
		return rpDTOList;
	}
}
