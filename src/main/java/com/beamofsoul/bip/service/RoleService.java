package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.Role;
import com.querydsl.core.types.Predicate;

public interface RoleService {

	Role create(Role role);
	Role update(Role role);
	long delete(Long... ids);
	
	List<Role> findAll();
	Page<Role> findAll(Pageable pageable);
	Page<Role> findAll(Pageable pageable, Predicate predicate);
	Role findByName(String name);
	Role findById(Long id);

	boolean checkRoleNameUnique(String roleName, Long roleId);
	boolean isUsedRoles(String roleIds);
}
