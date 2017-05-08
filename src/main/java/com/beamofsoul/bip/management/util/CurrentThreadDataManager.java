package com.beamofsoul.bip.management.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CurrentThreadDataManager
 * @Description 当前线程数据管理器
 * @author MingshuJian
 * @Date 2017年4月12日 上午9:55:11
 * @version 1.0.0
 */
public class CurrentThreadDataManager {

	private static ThreadLocal<Map<String, Object>> dataHolder = new ThreadLocal<>();
	
	public static Map<String, Object> getDataHolder() {
		Map<String, Object> dataMap = dataHolder.get();
		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
			dataHolder.set(dataMap);
		}
		return dataMap;
	}
	
	public static Object getData(String key) {
		return getDataHolder().get(key);
	}
	
	public static void setData(String key, Object value) {
		getDataHolder().put(key, value);
	}
	
	public static boolean containsKey(String key) {
		return getDataHolder().containsKey(key);
	}
}
