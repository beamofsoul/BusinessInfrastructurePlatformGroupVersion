package com.beamofsoul.bip.management.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.NonNull;

public class JSONUtils {
	
	/*
	 * Controller方法中需要返回分页JSON对象的默认对象名
	 */
	public static final String PAGEABLE_JSON_OBJECT_NAME = "pageableData";
	public static final String DELIMITER = ":";
	
	/**
	 * @Title: formatAndParseObject  
	 * @Description: 格式化并且解析JSON字符串并返回JSON对象
	 * @param jsonStr 格式化前的JSON字符串
	 * @param replaceFrom 需要在格式化过程中替换掉的字符，替换为JSON的默认分隔符
	 * @return JSONObject 格式化和解析后生成的JSON对象  
	 */
	public static JSONObject formatAndParseObject(String jsonStr, String replaceFrom) {
		jsonStr = jsonStr.replace(replaceFrom, DELIMITER);
		return JSONObject.parseObject(jsonStr);
	}
	
	/**
	 * @Title: formatAndParseObject  
	 * @Description: 格式化并且解析JSON字符串并返回JSON对象
	 * @param jsonStr 格式化前的JSON字符串
	 * @return JSONObject 格式化和解析后生成的JSON对象  
	 */
	public static JSONObject formatAndParseObject(String jsonStr) {
		return formatAndParseObject(jsonStr,"=");
	}
	
	/**
	 * @Title: formatAndParseArray  
	 * @Description: 格式化并且解析JSON字符串并返回JSON数组
	 * @param jsonStr 格式化前的JSON字符串
	 * @param replaceFrom 需要在格式化过程中替换掉的字符，替换为JSON的默认分隔符
	 * @return JSONArray 格式化和解析后生成的JSON数组
	 */
	public static JSONArray formatAndParseArray(String jsonStr, String replaceForm) {
		jsonStr = jsonStr.replace(replaceForm, DELIMITER);
		return JSONArray.parseArray(jsonStr);
	}
	
	/**
	 * @Title: formatAndParseArray  
	 * @Description: 格式化并且解析JSON字符串并返回JSON数组
	 * @param jsonStr 格式化前的JSON字符串
	 * @return JSONArray 格式化和解析后生成的JSON数组
	 */
	public static JSONArray formatAndParseArray(String jsonStr) {
		return formatAndParseArray(jsonStr,"=");
	}
	
	/**
	 * @Title: getJsonString  
	 * @Description: 根据回调方法返回自定义的JSON字符串  
	 * @param callback 回调方法
	 * @return String 返回的JSON字符串
	 */
	public static String getJsonString(Callback callback) {
    	JSONObject json = new JSONObject();
		callback.doCallback(json);
		return JSONUtils.toJSONString(json);
    }
	
	/**
	 * 根据输入参数JSONObject是否为null且是否size大于0判断是否该对象可用
	 * @param json 需要被判断的JSONObject对象
	 * @return boolean True:可用, false:不可用 
	 */
	public static boolean isNotBlank(JSONObject json) {
		return json != null && json.size() > 0;
	}
	
	/**
	 * 将输入参数JSONObject以JSON格式的String类型输出
	 * 其中, 关闭生成中为了优化而生成的代理引用对象
	 * (不能在页面中进行正常遍历输出,除非统一配置,所以索性就将此特定关闭)
	 * @param json 需要被格式化为String类型的JSONObject
	 * @return String - JSON格式的String类型实例
	 */
	public static String toJSONString(JSONObject json) {
		return JSONObject.toJSONString(json,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	/**
	 * 实例化一个JSONObject
	 * @return JSONObject实例
	 */
	public static JSONObject newInstance() {
		return new JSONObject();
	}
	
	/**
	 * 实例化一个JSONObject
	 * 然后将输入参数 value 按照 PAGEABLE_JSON_OBJECT_NAME 为 key存入到JSONObject中
	 * 并返回JSONObject对象
	 * @param value 默认key - PAGEABLE_JSON_OBJECT_NAME 需要的value
	 * @return 保存了键值对的JSONObject对象
	 */
	public static JSONObject newInstance(Object value) {
		return newInstance(PAGEABLE_JSON_OBJECT_NAME, value);
	}
	
	/**
	 * 实例化一个JSONObject
	 * 然后将输入参数按照key:value方式存入到JSONObject中并返回
	 * @param key
	 * @param value
	 * @return 保存了键值对的JSONObject对象
	 */
	public static JSONObject newInstance(String key, Object value) {
		JSONObject json = newInstance();
		json.put(key,value);
		return json;
	}
	
	/**
	 * 实例化一个JSONObject
	 * 然后将输入参数按照key:value方式存入到JSONObject中并返回
	 * @param keys
	 * @param values
	 * @return 保存了键值对的JSONObject对象
	 */
	public static JSONObject newInstance(String[] keys, Object... values) {
		JSONObject json = newInstance();
		for (int i = 0; i < keys.length; i++) json.put(keys[i], values[i]);
		return json;
	}
	

	/**
	 * 将JSONObject对象实例中的数据赋值给传入的T对象实例
	 * @param obj - JSONObject对象实例
	 * @param entity - T对象实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> void readValue(@NonNull JSONObject obj, @NonNull T entity) {
		try {
			// 根据传入Entity类型将json中数据解析并生成一个特定属性被赋值后的对象实例
			Class<? extends Object> clazz = entity.getClass();
			T entityTemp = (T) JSON.toJavaObject(obj, clazz);
			
			// 将新生成的对象实例的有效属性值覆盖传入对象的属性值, 更新传入对象属性
			Field[] fields = clazz.getDeclaredFields();
			Method[] methods = clazz.getDeclaredMethods();
			Map<String,Field> fieldsMap = new HashMap<String,Field>(fields.length);
			Map<String,Method> methodsMap = new HashMap<String,Method>(methods.length);
			
			for (Field field : fields) fieldsMap.put(field.getName().toUpperCase(), field);
			for (Method method : methods) methodsMap.put(method.getName().toUpperCase(), method);
			
			Field field = null;
			Method setMethod = null;
			Method getMethod = null;
			
			for (String attribute : obj.keySet()) {
				field = fieldsMap.get(attribute.toUpperCase());
				if (field != null) { 
					field.setAccessible(true);
					setMethod = methodsMap.get("SET"+attribute.toUpperCase());
					getMethod = methodsMap.get("GET"+attribute.toUpperCase());
					if (setMethod != null && getMethod != null) {
						setMethod.setAccessible(true);
						getMethod.setAccessible(true);
						setMethod.invoke(entity,getMethod.invoke(entityTemp));
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
