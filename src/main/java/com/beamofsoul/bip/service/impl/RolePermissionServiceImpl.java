package com.beamofsoul.bip.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.RolePermission;
import com.beamofsoul.bip.entity.dto.RolePermissionDTO;
import com.beamofsoul.bip.entity.dto.RolePermissionMappingDTO;
import com.beamofsoul.bip.entity.query.QPermission;
import com.beamofsoul.bip.entity.query.QRole;
import com.beamofsoul.bip.management.util.CacheUtils;
import com.beamofsoul.bip.management.util.CollectionUtils;
import com.beamofsoul.bip.management.util.RolePermissionsMapping;
import com.beamofsoul.bip.repository.RolePermissionRepository;
import com.beamofsoul.bip.service.PermissionService;
import com.beamofsoul.bip.service.RolePermissionService;
import com.beamofsoul.bip.service.RoleService;

@Service("rolePermissionService")
public class RolePermissionServiceImpl extends BaseAbstractServiceImpl implements RolePermissionService {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Override
	public List<RolePermissionDTO> findAllRolePermissionMapping() {
		return rolePermissionRepository.findAllRolePermissionMapping();
	}

	@Transactional
	@Override
	public boolean updateRolePermissionMapping(RolePermissionMappingDTO dto) {
		try {
			// 1. 获取原始数据库中的角色权限映射关系
			final List<RolePermission> originalList = rolePermissionRepository.findByPredicate(QRole.role.id.eq(dto.getRoleId()));
			final List<Long> originalIdList = originalList.stream().map(e -> e.getPermission().getId()).collect(Collectors.toList());
			final Long roleId = dto.getRoleId();
			final Set<Long> permissionIds = dto.getPermissionIds();
			// 2. 获取应该添加的映射关系
			List<RolePermission> neededList = permissionIds.stream().filter(e -> !originalIdList.contains(e)).map(e -> new RolePermission(roleService.findById(roleId), permissionService.findById(e))).collect(Collectors.toList());
			// 3. 获取应该抛弃的映射关系
			List<Long> abandonedList = originalIdList.stream().filter(e -> !permissionIds.contains(e)).collect(Collectors.toList());
			// 4. 执行增加与删除操作
			if (CollectionUtils.isNotBlank(neededList)) this.bulkCreate(neededList);
			if (CollectionUtils.isNotBlank(abandonedList)) this.deleteByRoleIdAndPermissionIds(roleId, abandonedList);
			// 5. 清除缓存中符合增加或删除条件的记录
			CacheUtils.remove("roleCache", roleId); 
			// 6. 重新加载系统角色权限映射
			this.refreshRolePermissionMapping();
			// 7. 返回是否增加并删除成功
			return true;
		} catch (Exception e) {
			logger.error("failed to update the mappings between role and permission entity", e);
			return false;
		}
	}
	
	@Override
	public Collection<RolePermission> bulkCreate(Collection<RolePermission> rolePermissions) {
		return rolePermissionRepository.bulkSave(rolePermissions);
	}
	
	@Override
	public Long deleteByRoleIdAndPermissionIds(Long roleId, Collection<Long> permissionIds) {
		//此处如果使用QRolePermission.role.id和QRolePermission.permission.id会出现异常
		//猜测为QueryDSL与Hibernate兼容问题或QueryDSL查询文件生成的问题
		//已经将问题在GitHub上进行了反应, 该问题的URL: https://github.com/querydsl/querydsl/issues/2102
		return rolePermissionRepository.deleteByPredicate(QRole.role.id.eq(roleId).and(QPermission.permission.id.in(permissionIds)));
	}

	@Override
	public void refreshRolePermissionMapping() {
		RolePermissionsMapping.refill(this.findAllRolePermissionMapping());
	}
}
