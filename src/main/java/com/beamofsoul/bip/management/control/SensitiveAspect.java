package com.beamofsoul.bip.management.control;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.management.util.SensitiveWordsMapping;

import jodd.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SensitiveAspect
 * @Description 操作敏感词过滤切面实现类
 * @author MingshuJian
 * @Date 2017年6月8日 下午1:36:29
 * @version 1.0.0
 */
@Slf4j
@Aspect
@Component
public class SensitiveAspect {
	
	final String FIELDS_SEPARATOR = ",";
	
	@Pointcut("@within(org.springframework.stereotype.Service) && !execution(* *.find*(..)) && !execution(* *.search*(..)) && !execution(* *.*Search*(..))")
	public void locateAllServiceImplMethods() {}
	
	@Before("locateAllServiceImplMethods()")
	public void doBefore(JoinPoint joinPoint) {
		Class<Sensitive> annotationClass = Sensitive.class; //注解类型
		Object targetClass = joinPoint.getTarget(); //拦截的实体类
		Class<?> targetClassClass = targetClass.getClass(); //拦截的实体类类型
		String targetMethodName = joinPoint.getSignature().getName(); //拦截的方法名称
		Object[] arguments = joinPoint.getArgs(); //拦截的方法参数
		Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes(); //拦截的方法参数类型
		Method targetMethod = null; //拦截的具体方法
		boolean isTargetMethodAnnotated = false; //拦截的具体方法是否被注解
		Parameter[] parameters = null; //Method类中Parameters对象
		Parameter parameter = null; //某个Parameter对象
		Object argument = null; //拦截的方法参数中某个参数对象
		Field[] accessibleFields = null; //拦截的方法参数中某个参数对象类型下所有可用属性
		Sensitive annotation = null; //拦截的目标对象上的具体注解对象，包括注解上的配置信息
		String[] filteredFields = null; //如果拦截的目标方法上的具体注解对象配置了fields属性，则该数组装载fields解析后的标识需要过滤的属性名数组
		boolean clear = false; //如果拦截的目标方法上的具体注解对象配置了clear属性，且值为true，则当过滤特定属性时，不采用替换方式，而采用擦除方式进行过滤

		/**
		 * 1. 判断输入参数前是否有Sensitive注解
		 * 1.1 判断是否Sensitive注解上配置了fields用以直接标识想过滤的具体属性
		 * 1.1.1 如果Sensitive注解上配置了合法的fields值，则解析后直接过滤目标对象对应的属性值
		 * 1.1.2 如果Sensitive注解上没有配置fields属性值，则判断输入参数类型上是否有Sensitive注解
		 * 1.1.2.1 如果类型上面有注解，则过滤该类型下所有可识别的属性值(暂定只有String类型)
		 * 1.1.2.2 如果类型上面没有注解，则判断是否该类型的某些属性上有Sensitive注解
		 * 1.1.2.2.1 如果属性上面有，则过滤该属性值
		 * 1.1.2.2.2 如果没有任何属相上有Sensitive属性，则过滤该类型下所有可识别的属性值 
		 */
		try {
			/**
			 * 当目标方法是不是public时或找不到对应方法时，获取到的targetMethod值为null，且不会抛出异常
			 */
			targetMethod = ReflectUtil.getMethod0(targetClassClass, targetMethodName, parameterTypes);
			isTargetMethodAnnotated = isAnnotatedBy(annotationClass, targetMethod);
			if (targetMethod != null) {
				parameters = targetMethod.getParameters();
				for (int i = 0; i < arguments.length; i++) {
					argument = arguments[i];
					parameter = parameters[i];
					/**
					 * 输入参数上面有Sensitive注解
					 */
					if (parameter.isAnnotationPresent(annotationClass) || isTargetMethodAnnotated) {
						accessibleFields = ReflectUtil.getAccessibleFields(parameter.getType());
						annotation = parameter.getAnnotation(annotationClass);
						filteredFields = annotation.fields().split(FIELDS_SEPARATOR);
						clear = annotation.clear();
						
						/**
						 * 判断是否Sensitive注解上配置了fields用以直接标识想过滤的具体属性
						 */
						if (filteredFields.length > 0) {
							for (Field field : accessibleFields)
								if (ArrayUtils.contains(filteredFields, field.getName()))
									filterFieldValue(argument, field, clear);
						} else {
							if (parameter.getType().isAnnotationPresent(annotationClass) || (countFieldsAnnotatedBy(annotationClass, accessibleFields) == 0)) {
								/**
								 * 被注解的输入参数类型上有Sensitive注解 或者 被注解的输入参数类型上没有Sensitive注解但是其下所有属性值上也都没有Sensitive注解，则过滤所有能够过滤的属性
								 */
								for (Field field : accessibleFields)
									filterFieldValue(argument, field, clear);
							} else {
								/**
								 * 被注解的输入参数类型上没有Sensitive注解，则过滤所有在属性上有Sensitive注解的属性值
								 */
								for (Field field : accessibleFields)
									if (field.isAnnotationPresent(annotationClass))
										filterFieldValue(argument, field, clear);
							}
						}
					}
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			log.error("failed to filter sensitive words for parameters of method ", targetMethodName, e);
		}
	}

	private boolean isAnnotatedBy(Class<Sensitive> annotationClass, Method targetMethod) {
		return targetMethod != null ? targetMethod.isAnnotationPresent(annotationClass) : Boolean.FALSE;
	}

	private long countFieldsAnnotatedBy(Class<Sensitive> annotationClass, Field[] accessibleFields) {
		return Arrays.asList(accessibleFields).stream().filter(e -> e.isAnnotationPresent(annotationClass)).count();
	}

	private void filterFieldValue(Object argument, Field field, boolean clear) throws IllegalAccessException {
		field.setAccessible(true);
		Object value = field.get(argument);
		if (value instanceof String)
			field.set(argument, clear ? SensitiveWordsMapping.clear(value.toString()) : SensitiveWordsMapping.filter(value.toString()));
	}
}
