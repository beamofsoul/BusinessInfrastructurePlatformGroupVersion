package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toBooleanValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Permission;
import com.beamofsoul.bip.entity.query.QPermission;
import com.beamofsoul.bip.management.cache.CacheEvictBasedCollection;
import com.beamofsoul.bip.management.cache.CacheableBasedPageableCollection;
import com.beamofsoul.bip.management.util.CollectionUtils;
import com.beamofsoul.bip.management.util.RolePermissionsMapping;
import com.beamofsoul.bip.repository.PermissionRepository;
import com.beamofsoul.bip.repository.RolePermissionRepository;
import com.beamofsoul.bip.service.PermissionService;
import com.beamofsoul.bip.service.RolePermissionService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.NonNull;

@Service("permissionService")
@CacheConfig(cacheNames = "permissionCache")
public class PermissionServiceImpl extends BaseAbstractServiceImpl implements PermissionService {
	
	public static final String CACHE_NAME = "permissionCache";

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private RolePermissionService rolePermissionService;
	
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Override
	@CachePut(key="#result.id")
	public Permission create(Permission permission) {
		return permissionRepository.save(permission);
	}

	@Override
	@CacheEvictBasedCollection(key="#p0")
	@Transactional
	public long delete(@NonNull Long[] ids) {
		long count = 0L;
		if (!isUsedPermissions(ids)) {
			count = permissionRepository.deleteByIds(ids);
			if (count > 0) RolePermissionsMapping.refill
				(rolePermissionService.findAllRolePermissionMapping());
		}
		return count;
	}

	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}
	
	@Override
	public List<Permission> findRelationalAll(Predicate predicate) {
		List<Permission> children = permissionRepository.findByPredicateAndSort(predicate, new Sort(Direction.ASC, "sort"));
		loadRelationalInformation(children);
		return children;
	}

	@Override
	@CachePut(key="#result.id")
	public Permission findByName(String name) {
		return permissionRepository.findByName(name);
	}
	
	@Override
	@Cacheable(key="#id")
	public Permission findById(Long id) {
		return permissionRepository.findById(id);
	}

	@Override
	@CacheableBasedPageableCollection(entity = Permission.class)
	@Transactional(readOnly=true)
	public Page<Permission> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@CacheableBasedPageableCollection(entity = Permission.class)
	@Transactional(readOnly=true)
	public Page<Permission> findAll(Pageable pageable, Predicate predicate) {
		return null;
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		QPermission permission = new QPermission("Permission");
		BooleanExpression exp = null;
		
		String name = content.getString("name");
		exp = addExpression(name, exp, permission.name.like(like(name)));
		
		String id = content.getString("id");
		exp = addExpression(id, exp, permission.id.eq(toLongValue(id)));
		
		String url = content.getString("url");
		exp = addExpression(url, exp, permission.url.like(like(url)));
		
		String action = content.getString("action");
		exp = addExpression(action, exp, permission.action.like(like(action)));
		
		String group = content.getString("group");
		exp = addExpression(group, exp, permission.group.like(like(group)));
		
		String parentId = content.getString("parentId");
		exp = addExpression(parentId, exp, permission.parentId.eq(toLongValue(parentId)));
		
		String resourceType = content.getString("resourceType");
		exp = addExpression(resourceType, exp, permission.resourceType.eq(resourceType));
		
		String available = content.getString("available");
		exp = addExpression(available, exp, permission.available.eq(toBooleanValue(available)));
		
		return exp;
	}
	
	@Override
	public BooleanExpression onRelationalSearch(JSONObject content) {
		return new QPermission("Permission").parentId.eq(content.getLongValue("parentId"));
	}
	
	@Override
	public List<Permission> findAllAvailableData() {
		return permissionRepository.findByPredicateAndSort(new QPermission("Permission").available.eq(true), new Sort(Direction.ASC, Arrays.asList("sort","group","id")));
	}

	@Transactional
	@CachePut(key="#permission.id")
	@Override
	public Permission update(Permission permission) {
		Permission originalPermission = permissionRepository.findOne(permission.getId());
		BeanUtils.copyProperties(permission, originalPermission);
		return permissionRepository.save(originalPermission);
	}

	@Override
	public boolean checkPermissionNameUnique(String permissionName, Long permissionId) {
		BooleanExpression predicate = QPermission.permission.name.eq(permissionName);
		if (permissionId != null) predicate = predicate.and(QPermission.permission.id.ne(permissionId));
		return permissionRepository.count(predicate) == 0;
	}

	@Override
	public boolean isUsedPermissions(Long... permissionId) {
		boolean isUsed = false;
		if (permissionId != null) {
			try {
				Predicate predicate = null;
				for (Long id : permissionId) {
					predicate = QPermission.permission.id.eq(id);
					//判断是否被者角色权限表使用
					if (CollectionUtils.isNotBlank(rolePermissionRepository.findByPredicate(predicate))) {
						isUsed = true;
						break;
					}
				}
			} catch (Exception e) {
				logger.error("wrong structure of permission ids when trying to check the usage of permission ids", e);
			}
		}
		return isUsed;
	}
	
	private void loadRelationalInformation(List<Permission> permissions) {
		permissions.stream().forEach(e -> {
			e.setCountOfChildren(permissionRepository.count(QPermission.permission.parentId.eq(e.getId())));
		});
	}
}
