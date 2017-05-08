package com.beamofsoul.bip.entity.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RolePermissionMappingDTO
 * @Description 角色权限实体类映射关系数据传输对象
 * @author MingshuJian
 * @Date 2017年2月21日 下午2:15:30
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionMappingDTO {

	private Long roleId;
	private Set<Long> permissionIds;
	
	public Set<Long> getPermissionIds() {
		return permissionIds == null ? new HashSet<Long>(0) : permissionIds;
	}
}
