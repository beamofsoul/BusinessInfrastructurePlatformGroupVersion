package com.beamofsoul.bip.management.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;
	
	public static void setApplicationContext0(@NonNull ApplicationContext applicationContext) {
		if (SpringUtils.applicationContext == null) SpringUtils.applicationContext = applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringUtils.setApplicationContext0(applicationContext);
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}
	
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name,clazz);
	}
}
