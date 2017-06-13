package com.beamofsoul.bip.management.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Attribute
 * @Description 为目标对象赋值为从当前HttpServletRequest中解析出的特定属性，如果RequestBody中没有相应的数据，则返回null
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:48:50
 * @version 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

	String value();
}
