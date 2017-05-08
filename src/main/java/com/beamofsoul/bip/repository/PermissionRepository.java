package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.Permission;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

/**
 * @ClassName PermissionRepository
 * @Description 系统权限持久化层接口
 * @author MingshuJian
 * @Date 2017年2月7日 上午11:12:56
 * @version 1.0.0
 */
@Repository
public interface PermissionRepository extends BaseMultielementRepository<Permission,Long> {

	Permission findById(Long roleId);
	Permission findByName(String name);
}
