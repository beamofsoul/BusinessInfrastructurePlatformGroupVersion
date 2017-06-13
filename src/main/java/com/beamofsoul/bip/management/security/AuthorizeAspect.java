package com.beamofsoul.bip.management.security;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @ClassName AuthorizeAspect
 * @Description 操作action方法运行前权限判断切面实现类
 * @author MingshuJian
 * @Date 2017年6月8日 下午1:36:29
 * @version 1.0.0
 */
@Aspect
@Component
public class AuthorizeAspect {
	
	@Resource
	private CustomPermissionEvaluator customPermissionEvaluator;
	
	@Pointcut(value="@annotation(authorize)")
	public void locateAnnotation(Authorize authorize) {}
	
	@Around("locateAnnotation(authorize)")
	public Object doAround(ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
		String permissionAction = authorize.value();
		boolean exception = authorize.exception();
		boolean hasPermission = customPermissionEvaluator.hasPermission(SecurityContextHolder.getContext().getAuthentication(), null, permissionAction);
		
		if (!hasPermission)
			if (exception)
				throw new AccessDeniedException("403");
			else
				return null;
		else
			return joinPoint.proceed();
	}
}
