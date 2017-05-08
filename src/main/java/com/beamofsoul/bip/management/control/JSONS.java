package com.beamofsoul.bip.management.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName JONS
 * @Description 细节控制注解，过滤Controller中返回的JSON数据，以达到权限资源过滤的要求。使用此注解要求目标方法返回JSONObject对象
 * @author MingshuJian
 * @Date 2017年2月17日 上午9:06:35
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONS {
	
	JSON[] value();
}
