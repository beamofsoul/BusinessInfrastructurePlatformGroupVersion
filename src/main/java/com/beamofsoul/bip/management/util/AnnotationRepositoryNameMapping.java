package com.beamofsoul.bip.management.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName AnnotationRepositoryClassMapping
 * @Description 注解'@Repository'的持久化类映射工具类
 * @author MingshuJian
 * @Date 2017年4月7日 下午2:37:33
 * @version 1.0.0
 */
@Slf4j
public class AnnotationRepositoryNameMapping {

	public static Map<String, String> repositoryMap = new HashMap<>();
	
	public static void loadRepositoryMap() {
		log.debug("开始加载持久化注解名称映射信息...");
		if (repositoryMap.size() > 0) repositoryMap.clear(); 
		Map<String, Object> beansWithAnnotation = SpringUtils.getApplicationContext().getBeansWithAnnotation(Repository.class);
		for (String key : beansWithAnnotation.keySet()) repositoryMap.put(key.replace("Repository", "").toLowerCase(), key);
		log.debug("持久化注解名称映射信息加载完毕...");
	}
}
