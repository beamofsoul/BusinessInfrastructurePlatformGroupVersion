package com.beamofsoul.bip.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beamofsoul.bip.entity.DetailControl;
import com.beamofsoul.bip.repository.DetailControlRepository;
import com.beamofsoul.bip.service.DetailControlService;

@Service("detailControlService")
public class DetailControlServiceImpl extends BaseAbstractServiceImpl implements DetailControlService {

	@Autowired
	private DetailControlRepository detailControlRepository;
	
	@Override
	public DetailControl findById(Long id) {
		return detailControlRepository.findOne(id);
	}

	@Override
	public List<DetailControl> findByRoleIdAndEntityClass(Long roleId,
			String entityClass) {
		return detailControlRepository.findByRoleIdAndEntityClassOrderByPriorityAsc(roleId,entityClass);
	}
	
	@Override
	public List<DetailControl> findByRoleIdAndEntityClass(Long roleId,
			String entityClass, Boolean enabled) {
		return detailControlRepository.findByRoleIdAndEntityClassAndEnabledOrderByPriorityAsc(roleId,entityClass,enabled);
	}
}
