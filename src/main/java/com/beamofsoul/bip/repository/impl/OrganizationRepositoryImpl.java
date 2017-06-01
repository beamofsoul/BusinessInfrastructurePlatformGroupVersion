package com.beamofsoul.bip.repository.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toBooleanValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;
import static com.beamofsoul.bip.management.util.QueryDSLUtils.*;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.entity.query.QDepartment;
import com.beamofsoul.bip.entity.query.QOrganization;
import com.beamofsoul.bip.repository.OrganizationRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.NonNull;

@NoRepositoryBean
public class OrganizationRepositoryImpl implements OrganizationRepositoryCustom {
	
	@Autowired
	private EntityManager entityManager;
	
	

//	@Override
//	public Page<UserRoleDTO> findAllUserRoleMapping(Pageable pageable) {
//		JPAQuery<UserRoleDTO> query = new JPAQuery<UserRoleDTO>(entityManager);
//		QUserRole userRole = QUserRole.userRole;
//		QueryResults<UserRoleDTO> qr = 
//				query.select(Projections.constructor(UserRoleDTO.class,
//						userRole.id,
//						userRole.user.id,
//						userRole.user.username,
//						userRole.user.nickname,
//						userRole.role.id,
//						userRole.role.name))
//					.from(userRole)
//					.offset(pageable.getOffset()).limit(pageable.getPageSize())
//					.orderBy(userRole.user.id.asc(),userRole.role.id.asc())
//					.fetchResults();
//		return new PageImpl<>(qr.getResults(),pageable,qr.getTotal());
//	}
	
	
//	@Override
//	
//	public Page<UserRoleCombineRole> findUserRoleMappingByConditionViaView(Pageable pageable, Object condition) {
//		Predicate predicate = QUserRoleCombineRole.userRoleCombineRole.roleId.contains(condition.toString());
//		return doQuery(entityManager, QUserRoleCombineRole.userRoleCombineRole,pageable,predicate);
//	}
	
	@Override
	public Page<Organization> findAllChildrenOrganizations(Pageable pageable, Object condition){
//		JPAQuery<Organization> query = new JPAQuery<Organization>(entityManager);
//		QOrganization organization = QOrganization.organization;
//		Predicate predicate = userRole.role.id.eq(Long.valueOf(condition.toString()));
//		QueryResults<Organization> qr =  
//				query.select(Projections.constructor(Organization.class,
//						organization.id,
//						organization.name))
//					.from(organization)
//					.where(predicate)
//					.offset(pageable.getOffset()).limit(pageable.getPageSize())
////					.orderBy(organization.id.asc())
//					.fetchResults();
//		return new PageImpl<>(qr.getResults(),pageable,qr.getTotal());
		return null;
	}
//	public Page<UserRoleDTO> findUserRoleMappingByCondition(Pageable pageable, @NonNull Object condition) {
//		JPAQuery<UserRole> query = new JPAQuery<UserRole>(entityManager);
//		QUserRole userRole = QUserRole.userRole;
//		Predicate predicate = userRole.role.id.eq(Long.valueOf(condition.toString()));
//		QueryResults<UserRoleDTO> qr =  
//				query.select(Projections.constructor(UserRoleDTO.class,
//						userRole.id,
//						userRole.user.id,
//						userRole.user.username,
//						userRole.user.nickname,
//						userRole.role.id,
//						userRole.role.name))
//					.from(userRole)
//					.where(predicate)
//					.offset(pageable.getOffset()).limit(pageable.getPageSize())
//					.orderBy(userRole.user.id.asc(),userRole.role.id.asc())
//					.fetchResults();
//		return new PageImpl<>(qr.getResults(),pageable,qr.getTotal());
//	}

//	@Override
//	public UserRoleCombineRole findUserRoleMappingByUserId(Long userId) {
//		Predicate predicate = QUserRoleCombineRole.userRoleCombineRole.userId.eq(userId);
//		return doQuery(entityManager, QUserRoleCombineRole.userRoleCombineRole, predicate);
//	}
	
//	@Override
//	public Organization findOrganizationMaxSort(Long parentId){
//		QOrganization organization = QOrganization.organization;
//		QOrganization d = new QOrganization("d");
//		Predicate predicate = organization.parentId.eq(parentId).and(organization.sort.eq(JPAExpressions.select(d.sort.max()).from(d).where(d.parentId.eq(parentId))));
//		return doQuery(entityManager, QOrganization.organization, predicate);
//	}
	
	@Override
	public Integer findOrganizationMaxSort(Long parentId){
		JPAQuery<Integer> query = new JPAQuery<Integer>(entityManager);
		QOrganization organization = QOrganization.organization;
//		return query.select(organization.sort.max()).from(organization).where(organization.parentId.eq(parentId)).fetchFirst();
		return query.select(organization.sort.max()).from(organization).where(organization.parent.id.eq(parentId)).fetchFirst();
	}
}
