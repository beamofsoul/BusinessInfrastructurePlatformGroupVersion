package com.beamofsoul.bip.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.UserRole;
import com.beamofsoul.bip.entity.UserRoleCombineRole;

public interface UserRoleService {

	Page<UserRoleCombineRole> findAllUserRoleMapping(Pageable pageable);
	Page<UserRoleCombineRole> findUserRoleMappingByCondition(Pageable pageable, Object condition);
	UserRoleCombineRole findUserRoleMappingByUserId(Long userId);
	UserRoleCombineRole updateUserRoleMapping(UserRoleCombineRole userRoleCombineRole);
	
	UserRole create(UserRole userRole);
	Collection<UserRole> bulkCreate(Collection<UserRole> userRoles);
	void delete(Long id);
	Long deleteByIds(Long... ids);
	Long deleteByUserIdAndRoleIds(Long userId, Long[] roleIds);
}
