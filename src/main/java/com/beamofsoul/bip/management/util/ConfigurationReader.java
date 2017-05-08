package com.beamofsoul.bip.management.util;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import lombok.NonNull;

/**
 * @ClassName CofigurationReader
 * @Description 针对Yaml配置文件解析的工具类
 * @author MingshuJian
 * @Date 2017年2月6日 下午2:08:17
 * @version 1.0.0
 */
public class ConfigurationReader {
	
	private static Map<String, ?> configMap = null;
	
	public static final String DEFAULT_CONFIGURATION_FILE_PATH = "application.yml";
	
	//有关生成与解析二维码相关的配置项
	public static final String PROJECT_COMPONENT_QRCODE_WIDTH = "project.component.qrcode.width";
	public static final String PROJECT_COMPONENT_QRCODE_HEIGHT = "project.component.qrcode.height";
	public static final String PROJECT_COMPONENT_QRCODE_MARGIN = "project.component.qrcode.margin";
	public static final String PROJECT_COMPONENT_QRCODE_SUFFIX = "project.component.qrcode.suffix";
	public static final String PROJECT_COMPONENT_QRCODE_DEFAULT_IMAGE = "project.component.qrcode.defaultImage";
	public static final String PROJECT_COMPONENT_QRCODE_REVERSAL_COLOR = "project.component.qrcode.reversalColor";
	
	public static final String PROJECT_BASE_REPOSITORY_FACTORY = "project.base.repository.factory";
	public static final String PROJECT_BASE_REPOSITORY_BATCH_SIZE = "project.base.repository.batch.size";
	public static final String PROJECT_BUSSINESS_USER_PHOTO_PATH = "project.business.user.photoPath";
	public static final String PROJECT_BUSSINESS_CONTENT_IMAGE_PATH = "project.business.content.imagePath";
	public static final String SPRING_MAIL_USERNAME = "spring.mail.username";
	public static final String DEFAULT_DELIMITER = "\\.";
	public static final String ROOT_PATH = "/";

	public static Object getValue(@NonNull String property) {
		return getValue(null, property);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getValue(String configurationFilePath, @NonNull String property) {
		Map<String, ?> resultMap = configMap == null ? getConfigurations(configurationFilePath) : configMap;
		String[] splitedPropArray = property.split(DEFAULT_DELIMITER);
		int length = splitedPropArray.length;
		String currentKey = null;
		for (int i = 0; i < length; i++) {
			if ((i + 1) == length) {
				return resultMap.get(splitedPropArray[i]);
			}
			currentKey = splitedPropArray[i];
			if (resultMap.containsKey(currentKey)) {
				resultMap = (Map) resultMap.get(currentKey);
			} else {
				break;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, ?> getConfigurations(String configurationFilePath) {
		if (configMap == null) {
			Object load = new Yaml().load(ConfigurationReader.class.getResourceAsStream(getPath(configurationFilePath)));
			configMap = load == null ? new HashMap<String, Object>() : (Map<String, ?>) load; 
		}
		return configMap;
	}
	
	public static String asString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static Integer asInteger(Object obj) {
		return obj == null || !(obj instanceof Integer) ? null : Integer.valueOf(obj.toString());
	}
	
	public static Boolean asBoolean(Object obj) {
		return obj == null ? null : (obj instanceof Boolean) ? Boolean.valueOf(obj.toString()) : obj.toString().equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
	}
	
	private static String getPath(String configurationFilePath) {
		return ROOT_PATH + ((configurationFilePath == null || configurationFilePath.trim().length() == 0) 
				? DEFAULT_CONFIGURATION_FILE_PATH : configurationFilePath); 
	}
	
}