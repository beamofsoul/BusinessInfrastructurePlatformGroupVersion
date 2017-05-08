package com.beamofsoul.bip.management.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.NonNull;

public class PageUtils {

	public static final String PAGEABLE_PAGE_NUMBER_NAME = "page";
	public static final String PAGEABLE_PAGE_SIZE_NAME = "size";
	public static final String PAGEABLE_SORT_BY_NAME = "sort";
	public static final String PAGEABLE_SORT_DIRECTION_NAME = "direction";

	public static int getFirstResult(int pageNumber, int pageSize) {
		return pageNumber * pageSize;
	}

	public static int getFirstResult(@NonNull Pageable pageable) {
		return getFirstResult(pageable.getPageNumber(), pageable.getPageSize());
	}

	public static int getMaxResult(int pageNumber, int pageSize) {
		// return (pageNumber + 1) * pageSize - 1;
		return (pageNumber + 1) * pageSize;
	}

	public static int getMaxResult(@NonNull Pageable pageable) {
		return getMaxResult(pageable.getPageNumber(), pageable.getPageSize());
	}

	public static int getTotalPages(int pageSize, long recordsCount) {
		return (int) Math.ceil((float) recordsCount / (pageSize > 0 ? pageSize : 1));
	}

	public static int getTotalPages(@NonNull Pageable pageable, long recordsCount) {
		return getTotalPages(pageable.getPageSize(), recordsCount);
	}
	
	/**
	 * @Title: setPageableSort  
	 * @Description: 根据输入的分页对象与排序对象重置分页对象的排序条件
	 * @param pageable - 重置前的分页对象
	 * @param sort - 新的排序对象
	 * @return Pageable - 重置后的分页对象  
	 */
	public static Pageable setPageableSort(@NonNull Pageable pageable, Sort sort) {
		return parsePageable(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	/**
	 * @Title: parsePageable  
	 * @Description: 根据输入参数Map，获取排序列与升降序参数  
	 * @param map 输入参数
	 * @return Pageable 返回类型  
	 */
	public static Pageable parsePageable(Map<String, Object> map) {
		Object sortByStr = map.get(PAGEABLE_SORT_BY_NAME);
		if (sortByStr == null || (sortByStr != null && StringUtils.isBlank(sortByStr.toString()))) {
			return parsePageable(map, Constants.DEFAULT_ENTITY_PRIMARY_KEY);
		} else {
			Object directionNum = map.get(PAGEABLE_SORT_DIRECTION_NAME);
			Direction direction = Direction.ASC;
			if (directionNum != null && directionNum.equals(1)) {
				direction = Direction.DESC;
			}
			return parsePageable(map, sortByStr.toString().trim(), direction);
		}
	}

	public static Pageable parsePageable(Map<String, Object> map, String sortBy) {
		return parsePageable(map, new Sort(Direction.ASC, sortBy));
	}

	public static Pageable parsePageable(Map<String, Object> map, Direction direction) {
		return parsePageable(map, new Sort(direction, Constants.DEFAULT_ENTITY_PRIMARY_KEY));
	}

	public static Pageable parsePageable(Map<String, Object> map, String sortBy, Direction direction) {
		return parsePageable(map, new Sort(direction, sortBy));
	}

	public static Pageable parsePageable(Map<String, Object> map, Sort sort) {
		return parsePageable(Integer.valueOf(map.get(PAGEABLE_PAGE_NUMBER_NAME).toString()), Integer.valueOf(map.get(PAGEABLE_PAGE_SIZE_NAME).toString()), sort);
	}

	public static Pageable parsePageable(int pageNumber, int pageSize, String sortBy) {
		return parsePageable(pageNumber, pageSize, new Sort(Direction.ASC, sortBy));
	}

	public static Pageable parsePageable(int pageNumber, int pageSize, Direction direction) {
		return parsePageable(pageNumber, pageSize, new Sort(direction, Constants.DEFAULT_ENTITY_PRIMARY_KEY));
	}
	
	public static Pageable parsePageable(int pageNumber, int pageSize, Direction direction, String sortBy) {
		return parsePageable(pageNumber, pageSize, new Sort(direction, sortBy));
	}

	public static Pageable parsePageable(int pageNumber, int pageSize, Sort sort) {
		return new PageRequest(pageNumber, pageSize, sort);
	}
}
