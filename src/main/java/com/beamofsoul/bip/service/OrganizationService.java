package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Organization;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface OrganizationService {

	Organization create(Organization organization);
	Organization update(Organization organization);
//	Organization moveUp(Organization organization);
	List<Organization> changeSort(Long beforeId,Long afterId);
	long delete(Long... ids);
	long deleteNodes(List<Long> parentIds,List<Long> childrenIds);
//	long deleteNodes(Long[] parentIds,Long[] childrenIds);

	Organization findById(Long id);

	List<Organization> findAll();
	List<Organization> findRelationalAll(Predicate predicate);
	Page<Organization> findAll(Pageable pageable);
	Page<Organization> findAll(Pageable pageable, Predicate predicate);
	Page<Organization> findAllChildrenOrganizations(Pageable pageable, Object condition);
	List<Organization> findAllAvailableOrganizations();
	
	BooleanExpression onSearch(JSONObject content,List<Long> idsLong);
	BooleanExpression onRelationalSearch(JSONObject content);
}
