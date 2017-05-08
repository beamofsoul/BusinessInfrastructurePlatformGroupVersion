package com.beamofsoul.bip.management.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName JSON
 * @Description 细节控制注解，过滤Controller中返回的JSON数据，以达到权限资源过滤的要求。使用此注解要求目标方法返回JSONObject对象
 * @author MingshuJian
 * @Date 2017年2月16日 下午4:13:35
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JSONS.class)
public @interface JSON {
	
	Class<?> type();
	String include() default "";
	String filter() default "";
}
