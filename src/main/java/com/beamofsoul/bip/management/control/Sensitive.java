package com.beamofsoul.bip.management.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Sensitive
 * @Description 敏感词过滤注解类
 * @author MingshuJian
 * @Date 2017年6月1日 下午2:34:15
 * @version 1.0.0
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

}
