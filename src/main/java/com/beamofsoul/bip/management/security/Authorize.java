package com.beamofsoul.bip.management.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName PreAuth
 * @Description 根据输入的权限action，判断当前用户是否已经登录并有权限进入当前目标方法，如果没有权限则抛出异常，或通过配置使方法返回null
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:48:50
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Authorize {

	String value();
	boolean exception() default true;
}
