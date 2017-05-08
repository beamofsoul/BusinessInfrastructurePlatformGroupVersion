package com.beamofsoul.bip.management.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.beamofsoul.bip.entity.ActionMonitor;

/**
 * @ClassName Monitoring
 * @Description 操作行为监控注解类
 * @author MingshuJian
 * @Date 2017年4月12日 下午1:38:15
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitoring {

	Class<?> target() default Object.class;
	ActionMonitor.HazardLevel hazardLevel() default ActionMonitor.HazardLevel.INSIGNIFICANT;
	String key() default "";
	String effect() default "";
}
