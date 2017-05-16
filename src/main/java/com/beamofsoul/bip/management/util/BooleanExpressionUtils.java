package com.beamofsoul.bip.management.util;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * @ClassName BooleanExpressionUtils
 * @Description 当使用BooleanExpression进行查询配置时，使用的工具类
 * @author MingshuJian
 * @Date 2017年5月15日 下午4:10:17
 * @version 1.0.0
 */
public class BooleanExpressionUtils {
	
	/**
	 * @Title: addExpression  
	 * @Description: 整合判断表达式  
	 * @param value 需要根据其值判断是否执行整合过程
	 * @param exp 之前的表达式
	 * @param subExp 最新的表达式
	 * @return BooleanExpression 整合后的表达式  
	 */
	public static BooleanExpression addExpression(String value, BooleanExpression exp, BooleanExpression subExp) {
		return StringUtils.isNotBlank(value) ? addSubExpression(exp, subExp) : exp;
	}
	
	/**
	 * @Title: addSubExpression  
	 * @Description: 整合判断表达式  
	 * @param exp 之前的表达式
	 * @param subExp 最新的表达式
	 * @return BooleanExpression 整合后的表达式  
	 */
	public static BooleanExpression addSubExpression(BooleanExpression exp, BooleanExpression subExp) {
		return exp == null ? subExp : exp.and(subExp);
	}
	
	/**
	 * @Title: like  
	 * @Description: 拼接两侧like字符串 
	 * @param property
	 * @return String 拼接好的like字符串
	 */
	public static String like(String property) {
		return "%" + property + "%";
	}
	
	/**
	 * @Title: like  
	 * @Description: 拼接左侧或右侧like字符串 
	 * @param property
	 * @return String 拼接好的like字符串
	 */
	public static String like(String property, boolean leftLike) {
		return leftLike ? "%" + property : property + "%";
	}
	
	/**
	 * @Title: toLongValue  
	 * @Description: 当输入值不为空或""时，将其转换为长整形并返回，否则返回0L  
	 * @param value 需要被转型的字符串
	 * @return Long 转型后的长整形  
	 */
	public static Long toLongValue(String value) throws NumberFormatException {
		return StringUtils.isNotBlank(value) ? Long.valueOf(value) : 0L;
	}
	
	/**
	 * @Title: toBooleanValue  
	 * @Description: 当输入值不为空或""时，将其转换为Boolean类型对象并返回，否则返回false  
	 * @param value 需要被转型的字符串
	 * @return Boolean 转型后的Boolean类型对象
	 */
	public static Boolean toBooleanValue(String value) {
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : Boolean.FALSE;
	}
}
