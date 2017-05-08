package com.beamofsoul.bip.entity.dto;

import com.beamofsoul.bip.entity.Permission;
import com.beamofsoul.bip.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force=true)
@AllArgsConstructor
public class RolePermissionDTO {

	private Long roleId;
	private String roleName;
	private Long permissionId;
	private String permissionName;
	private String permissionAction;
	private String permissionUrl;
	private String permissionResourceType;
	private Long permissionParentId;
	private String permissionGroup;
	private Long permissionSort;
	private Boolean permissionAvailable;

	public RolePermissionDTO(Long roleId, String roleName,
			Long permissionId, String permissionName,
			Boolean permissionAvailable, Long permissionParentId,
			String permissionResourceType, String permissionUrl,
			String permissionAction, Long permissionSort) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.permissionAvailable = permissionAvailable;
		this.permissionParentId = permissionParentId;
		this.permissionResourceType = permissionResourceType;
		this.permissionUrl = permissionUrl;
		this.permissionAction = permissionAction;
		this.permissionSort = permissionSort;
	}

	public Role convertToRole() {
		return new Role(roleId, roleName);
	}

	public Permission convertToPermission() {
		return new Permission(permissionId, permissionName, permissionAction,
				permissionUrl, permissionResourceType, permissionParentId,
				permissionGroup, permissionSort, permissionAvailable);
	}
}
