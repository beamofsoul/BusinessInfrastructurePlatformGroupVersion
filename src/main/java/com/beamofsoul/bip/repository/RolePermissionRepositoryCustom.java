package com.beamofsoul.bip.repository;

import java.util.List;

import com.beamofsoul.bip.entity.dto.RolePermissionDTO;

public interface RolePermissionRepositoryCustom<T,ID> {

	List<RolePermissionDTO> findAllRolePermissionMapping();
}
