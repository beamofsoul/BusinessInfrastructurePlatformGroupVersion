package com.beamofsoul.bip.repository;


import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.Role;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

/**
 * @ClassName RoleRepository
 * @Description 系统角色持久化层接口
 * @author MingshuJian
 * @Date 2017年2月7日 上午11:12:56
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends BaseMultielementRepository<Role,Long> {

	Role findByName(String name);
}
