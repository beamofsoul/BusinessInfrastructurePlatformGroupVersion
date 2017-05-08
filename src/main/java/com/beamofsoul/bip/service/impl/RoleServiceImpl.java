package com.beamofsoul.bip.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.Role;
import com.beamofsoul.bip.entity.query.QRole;
import com.beamofsoul.bip.management.cache.CacheEvictBasedCollection;
import com.beamofsoul.bip.management.cache.CacheableBasedPageableCollection;
import com.beamofsoul.bip.management.util.CollectionUtils;
import com.beamofsoul.bip.management.util.RolePermissionsMapping;
import com.beamofsoul.bip.repository.RolePermissionRepository;
import com.beamofsoul.bip.repository.RoleRepository;
import com.beamofsoul.bip.repository.UserRoleRepository;
import com.beamofsoul.bip.service.RolePermissionService;
import com.beamofsoul.bip.service.RoleService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.NonNull;

@Service("roleService")
@CacheConfig(cacheNames = "roleCache")
public class RoleServiceImpl extends BaseAbstractServiceImpl implements RoleService {
	
	public static final String CACHE_NAME = "roleCache";

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RolePermissionService rolePermissionService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Override
	@CachePut(key="#result.id")
	public Role create(Role role) {
		return roleRepository.save(role);
	}

	@Override
	@CacheEvictBasedCollection(key="#p0")
	@Transactional
	public long delete(@NonNull Long... ids) {
		try {
			if (ids.length > 0) {
				long count = roleRepository.deleteByIds(ids);
				if (count > 0) 
					RolePermissionsMapping.refill(rolePermissionService.
							findAllRolePermissionMapping());
				return count;
			} else {
				throw new Exception("failed to delete roles because zero-length of role ids");
			}
		} catch (Exception e) {
			logger.error("role ids must be not zero-length when deleting...", e);
		}
		return 0;
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	@CachePut(key="#result.id")
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}
	
	@Override
	@Cacheable(key="#id")
	public Role findById(Long id) {
		return roleRepository.findOne(id);
	}

	@Override
	@CacheableBasedPageableCollection(entity = Role.class)
	@Transactional(readOnly=true)
	public Page<Role> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@CacheableBasedPageableCollection(entity = Role.class)
	@Transactional(readOnly=true)
	public Page<Role> findAll(Pageable pageable, Predicate predicate) {
		return null;
	}

	@Transactional
	@CachePut(key="#role.id")
	@Override
	public Role update(Role role) {
		Role originalRole = roleRepository.findOne(role.getId());
		BeanUtils.copyProperties(role, originalRole);
		return roleRepository.save(originalRole);
	}

	@Override
	public boolean checkRoleNameUnique(String roleName, Long roleId) {
		BooleanExpression predicate = QRole.role.name.eq(roleName);
		if (roleId != null) predicate = predicate.and(QRole.role.id.ne(roleId));
		return roleRepository.count(predicate) == 0;
	}

	@Override
	public boolean isUsedRoles(String roleIds) {
		boolean isUsed = false;
		if (StringUtils.isNotBlank(roleIds)) {
			String[] idsStr = roleIds.split(",");
			try {
				Predicate predicate = null;
				for (String idStr : idsStr) {
					predicate = QRole.role.id.eq(Long.valueOf(idStr));
					//判断是否被用户角色表或者角色权限表使用
					if (CollectionUtils.isNotBlank(userRoleRepository.findByPredicate(predicate)) || 
							CollectionUtils.isNotBlank(rolePermissionRepository.findByPredicate(predicate))) {
						isUsed = true;
						break;
					}
				}
			} catch (Exception e) {
				logger.error("wrong structure of role ids when trying to check the usage of role ids", e);
			}
		}
		return isUsed;
	}
}
