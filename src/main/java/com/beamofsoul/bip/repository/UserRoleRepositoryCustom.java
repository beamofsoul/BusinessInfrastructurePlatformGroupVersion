package com.beamofsoul.bip.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.UserRoleCombineRole;
import com.beamofsoul.bip.entity.dto.UserRoleDTO;

public interface UserRoleRepositoryCustom {
	
	Page<UserRoleDTO> findAllUserRoleMapping(Pageable pageable);
	Page<UserRoleCombineRole> findAllUserRoleMappingViaView(Pageable pageable);
	Page<UserRoleDTO> findUserRoleMappingByCondition(Pageable pageable, Object condition);
	Page<UserRoleCombineRole> findUserRoleMappingByConditionViaView(Pageable pageable, Object condition);
	
	UserRoleCombineRole findUserRoleMappingByUserId(Long userId);
}
