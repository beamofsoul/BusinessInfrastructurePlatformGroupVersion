package com.beamofsoul.bip.management.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.management.util.RolePermissionsMapping;

/**
 * @ClassName CustomPermissionEvaluator
 * @Description 自定义权限鉴别器
 * @author MingshuJian
 * @Date 2017年1月19日 下午4:25:34
 * @version 1.0.0
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

	/*
	 * (非 Javadoc)  
	 * <p>Title: hasPermission</p>  
	 * <p>Description: 判断用户是否拥有权限</p>  
	 * @param authentication
	 * @param targetDomainObject
	 * @param permission
	 * @return  
	 * @see org.springframework.security.access.PermissionEvaluator#hasPermission(org.springframework.security.core.Authentication, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		return RolePermissionsMapping.actionContains(authentication.getAuthorities(), permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		// not supported
		return false;
	}
}
