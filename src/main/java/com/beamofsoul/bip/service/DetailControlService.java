package com.beamofsoul.bip.service;

import java.util.List;

import com.beamofsoul.bip.entity.DetailControl;

public interface DetailControlService {

	DetailControl findById(Long id);
	List<DetailControl> findByRoleIdAndEntityClass(Long roleId,String entityClass);
	List<DetailControl> findByRoleIdAndEntityClass(Long roleId,String entityClass,Boolean enabled);
}
