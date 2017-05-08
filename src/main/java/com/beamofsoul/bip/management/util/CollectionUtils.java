package com.beamofsoul.bip.management.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName CollectionUtils
 * @Description 实现Collection接口实现类对象的工具类
 * @author MingshuJian
 * @Date 2017年2月16日 下午1:53:41
 * @version 1.0.0
 */
public class CollectionUtils {
	
	public static <T> boolean isBlank(Collection<T> collection) {
		boolean isBlank = false;
		if (collection == null) {
			isBlank = true;
		} else if (collection.size() == 0) {
			isBlank = true;
		} else {
			if (collection instanceof List) {
				List<T> list = new ArrayList<>(collection);
				boolean isEmptyList = true;
				for (Object obj : list) {
					if (obj != null) {
						if (obj instanceof String && StringUtils.isBlank(obj.toString())) {
							continue;
						}
						isEmptyList = false;
						break;
					}
				}
				isBlank = isEmptyList;
			}
		}
		return isBlank;
	}
	
	public static <T> boolean isNotBlank(Collection<T> collection) {
		return !isBlank(collection);
	}
}
