package com.beamofsoul.bip.management.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName AnnotationServiceNameMapping
 * @Description 注解'@Service'的服务类映射工具类
 * @author MingshuJian
 * @Date 2017年4月7日 下午2:37:33
 * @version 1.0.0
 */
@Slf4j
public class AnnotationServiceNameMapping {

	public static Map<String, String> serviceMap = new HashMap<>();
	
	public static void loadServiceMap() {
		log.debug("开始加载服务注解名称映射信息...");
		if (serviceMap.size() > 0) serviceMap.clear(); 
		Map<String, Object> beansWithAnnotation = SpringUtils.getApplicationContext().getBeansWithAnnotation(Service.class);
		for (String key : beansWithAnnotation.keySet()) serviceMap.put(key.replace("Service", "").toLowerCase(), key);
		log.debug("服务注解名称映射信息加载完毕...");
	}
}
