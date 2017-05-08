package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Permission;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface PermissionService {

	Permission create(Permission permission);
	Permission update(Permission permission);
	long delete(Long[] ids);
	
	List<Permission> findAll();
	List<Permission> findRelationalAll(Predicate predicate);
	Page<Permission> findAll(Pageable pageable);
	Page<Permission> findAll(Pageable pageable, Predicate predicate);
	BooleanExpression onSearch(JSONObject content);
	BooleanExpression onRelationalSearch(JSONObject content);
	List<Permission> findAllAvailableData();
	Permission findByName(String name);
	Permission findById(Long id);

	boolean checkPermissionNameUnique(String permissionName, Long permissionId);
	boolean isUsedPermissions(String permissionIds);

}
