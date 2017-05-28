package com.beamofsoul.bip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.DetailControl;

@Repository
public interface DetailControlRepository extends JpaRepository<DetailControl,Long>,JpaSpecificationExecutor<DetailControl> {

	List<DetailControl> findByRoleIdAndEntityClassOrderByPriorityAsc(Long roleId, String entityClass);
	List<DetailControl> findByRoleIdAndEntityClassAndEnabledOrderByPriorityAsc(Long roleId, String entityClass, Boolean enabled);
}
