package com.beamofsoul.bip.management.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * @ClassName CustomJSONFilter
 * @Description 自定义JSON过滤器
 * @author MingshuJian
 * @Date 2017年2月17日 上午8:46:31
 * @version 1.0.0
 */
@SuppressWarnings("deprecation")
@JsonFilter("customJSONFilter")
public class CustomJSONFilter extends FilterProvider {
	
	Map<Class<?>, Set<String>> includeMap = new HashMap<>();
	Map<Class<?>, Set<String>> filterMap = new HashMap<>();
	
	public void include(Class<?> type, String[] fields) {
		addToMap(includeMap, type, fields);
	}
	
	public void filter(Class<?> type, String[] fields) {
		addToMap(filterMap, type, fields);
	}
	
	private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
		Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
		fieldSet.addAll(Arrays.asList(fields));
		map.put(type, fieldSet);
	}

	@Override
	public BeanPropertyFilter findFilter(Object filterId) {
		throw new UnsupportedOperationException("Not supported that access to filters which has been deprecated");
	}

	@Override
	public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
		return new SimpleBeanPropertyFilter() {
			@Override
			public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider,
					PropertyWriter writer) throws Exception {
				if (apply(pojo.getClass(), writer.getName())) {
					writer.serializeAsField(pojo, jgen, provider);
				} else if (!jgen.canOmitFields()) {
					writer.serializeAsOmittedField(pojo, jgen, provider);
				} 
//				else { //打印被过滤掉的字段
//					System.out.println(writer.getName());
//				}
			}
		};
	}
	
	public boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> filterFields = filterMap.get(type);
        if (includeFields != null && includeFields.contains(name)) {
            return true;
        } else if (filterFields != null && !filterFields.contains(name)) {
            return true;
        } else if (includeFields == null && filterFields == null) {
            return true;
        }
        return false;
    }

}
