package com.beamofsoul.bip.management.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import lombok.NonNull;

public class CommonUtils {
	
	public static boolean isNotBlank(Object... objects) {
		return objects != null && objects.length > 0;
	}
	
	public static boolean isBlank(Object... objects) {
		return objects == null || (objects != null && objects.length == 0);
	}
	
	public static Long getLongEntityId(@NonNull Map<String, Object> map) {
			Object entityId = map.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY);
			return entityId == null ? 0L : Long.valueOf(entityId.toString());
	}

	public static List<Long> getIds(@NonNull List<?> list) {
		List<Long> returnList = new ArrayList<Long>(list.size());
		try {
			for (Object object : list) {
				Method method = object.getClass().getMethod("getId", new Class[]{});
				if (method != null)
					returnList.add(Long.valueOf(method.invoke(object).toString()));
			}
		} catch (Exception e) {
			throw new RuntimeException("转换对象Id不是长整形",e);
		}
		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAttributeList(@NonNull List<?> list,@NonNull String attributeName) {
		List<T> returnList = new ArrayList<T>(list.size());
		Method method = null;
		try {
			if (list.size() > 0 && list.get(0) != null) {
				method = list.get(0).getClass().getMethod("get" + StringUtils.capitalize(attributeName), new Class[]{});
			}
			for (Object object : list) {
				returnList.add((T) method.invoke(object));
			}
		} catch (Exception e) {
			throw new RuntimeException("反射方法执行失败",e);
		}
		return returnList;
	}
}
