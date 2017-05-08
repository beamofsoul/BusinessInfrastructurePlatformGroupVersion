package com.beamofsoul.bip.management.util;

import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DatabaseTableEntityMapping
 * @Description 数据库表和实体类映射工具类
 * @author MingshuJian
 * @Date 2017年4月7日 上午11:08:24
 * @version 1.0.0
 */
@Slf4j
public class DatabaseTableEntityMapping {

	public static Map<String, String> tableEntityMap;
	
	public static void initTableEntityMap() {
		if (tableEntityMap == null) {
			log.debug("开始加载数据库表与业务实体类映射信息...");
			tableEntityMap = 
				DatabaseUtils.entityManager.getMetamodel().getEntities()
				.stream().collect(
					Collectors.toMap(
						e-> e.getJavaType().getAnnotation(Table.class).name().toLowerCase(),
						e-> e.getJavaType().getName()));
			log.debug("数据库表与业务实体类映射信息加载完毕...");
		}
	}
	
	public static String getEntity(String tableName) {
		initTableEntityMap();
		return tableEntityMap.get(tableName);
	}
}
