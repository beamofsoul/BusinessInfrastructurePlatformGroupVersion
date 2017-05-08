package com.beamofsoul.bip.management.control;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName CustomJsonSerializer
 * @Description 为了细节控制自定义的JSON序列化器
 * @author MingshuJian
 * @Date 2017年2月16日 下午4:24:24
 * @version 1.0.0
 */
public class CustomJSONSerializer {

	private ObjectMapper mapper = new ObjectMapper();
	private CustomJSONFilter JSONFilter = new CustomJSONFilter();
	private static final String DELIMITER = ",";
	
	public void filter(JSON json) {
		this.filter(json.type(), json.include(), json.filter());
	}
	
	public void filter(Class<?> clazz, String include, String filter) {
		if (clazz == null) return;
		if (StringUtils.isNotBlank(include)) JSONFilter.include(clazz, include.split(DELIMITER));
		if (StringUtils.isNotBlank(filter)) JSONFilter.filter(clazz, filter.split(DELIMITER));
		mapper.addMixIn(clazz, JSONFilter.getClass());
	}
	
	public String toJSON(Object object) throws JsonProcessingException {
		return mapper.setFilterProvider(JSONFilter).writeValueAsString(object);
	}
}
