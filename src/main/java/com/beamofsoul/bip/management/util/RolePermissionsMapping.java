package com.beamofsoul.bip.management.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.security.core.GrantedAuthority;

import com.beamofsoul.bip.entity.Permission;
import com.beamofsoul.bip.entity.dto.RolePermissionDTO;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RolePermissionsMapping {
	
	private final static Map<String,List<Permission>> ROLE_PERMISSIONS_MAP =
			new TreeMap<String, List<Permission>>(String.CASE_INSENSITIVE_ORDER);
	
	private final static String SYSTEM_ACTION_OR_URL_PREFIX = "sys";
	private final static String ACTION_DELIMITER = ":";
	private final static String URL_DELIMITER = "/";
	
	public static boolean actionContains(Collection<? extends GrantedAuthority> roles, @NonNull Object permissionAction) {
		String requiredSystemAction = getSystemActionOrURL(permissionAction, true);
		for (Permission permission : getCurrentUserAllPermissions(roles)) {
			if (permission.getAction() == null)
				continue;
			if (permission.getAction().equals(requiredSystemAction) || permission.getAction().equals(permissionAction) )
				return true;
		}
		return false;
	}
	
	public static boolean urlContains(Set<GrantedAuthority> roles,Object permissionURL) {
		String requiredSystemURL = getSystemActionOrURL(permissionURL, false);
		for (Permission permission : getCurrentUserAllPermissions(roles)) {
			if (permission.getUrl() == null)
				continue;
			if (permission.getUrl().equals(requiredSystemURL) || permission.getUrl().equals(permissionURL))
				return true;
		}
		return false;
	}

	private static Set<Permission> getCurrentUserAllPermissions(
			Collection<? extends GrantedAuthority> roles) {
		Set<Permission> allPermissionsUserHas = new HashSet<Permission>();
		for (GrantedAuthority role : roles)
			allPermissionsUserHas.addAll(ROLE_PERMISSIONS_MAP.get(role.getAuthority().split("_")[1]));
		return allPermissionsUserHas;
	}
	
	public static void fill(List<RolePermissionDTO> rps) {
		log.debug("开始加载角色权限映射信息...");
		for (RolePermissionDTO rp : rps) {
			String roleName = rp.getRoleName();
			if (!ROLE_PERMISSIONS_MAP.containsKey(roleName))
				ROLE_PERMISSIONS_MAP.put(roleName, new ArrayList<Permission>());
			ROLE_PERMISSIONS_MAP.get(roleName).add(rp.convertToPermission());
		}
		log.debug("角色权限映射信息加载完毕...");
	}
	
	public static void refill(List<RolePermissionDTO> rps) {
		ROLE_PERMISSIONS_MAP.clear();
		fill(rps);
	}
	
	private static String getPureOperation(Object permission, boolean isAction) {
		return permission.toString().split(isAction ? ACTION_DELIMITER : URL_DELIMITER)[1];
	}
	
	private static String getSystemOperation(String pure, boolean isAction) {
		return SYSTEM_ACTION_OR_URL_PREFIX + (isAction ? ACTION_DELIMITER : URL_DELIMITER) + pure;
	}
	
	private static String getSystemActionOrURL(Object permissionStr, boolean isAction) {
		return getSystemOperation(getPureOperation(permissionStr, isAction), isAction);
	}
}
