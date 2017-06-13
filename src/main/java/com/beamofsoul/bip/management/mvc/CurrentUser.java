package com.beamofsoul.bip.management.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName CurrentUser
 * @Description 为目标对象赋值为当前登录用户，如果当前没有登录用户赋值为null
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:48:50
 * @version 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

}
