package com.beamofsoul.bip.management.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.management.control.CustomHttpServletRequestWrapper;

/**
 * @ClassName SettingAttributeMethodArgumentResolver
 * @Description 自定义注解Attribute作为方法参数时的解析器
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:52:08
 * @version 1.0.0
 */
public class SettingAttributeMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Attribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request instanceof CustomHttpServletRequestWrapper) {
			String requestBody = new String(((CustomHttpServletRequestWrapper) request).getRequestBody());
			JSONObject attributes = JSONObject.parseObject(requestBody);
			
			Attribute parameterAnnotation = parameter.getParameterAnnotation(Attribute.class);
			String key = parameterAnnotation.value();
			if (attributes.containsKey(key)) {
				Class<?> parameterType = parameter.getParameterType();
				if (parameterType.equals(String.class)) return attributes.get(key).toString();
				else if (parameterType.equals(Long.class)) return Long.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Integer.class)) return Integer.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Double.class)) return Double.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Float.class)) return Float.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Boolean.class)) return Boolean.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Byte.class)) return Byte.valueOf(attributes.get(key).toString());
				else if (parameterType.equals(Short.class)) return Short.valueOf(attributes.get(key).toString());
			}
		}
		return null;
	}
}
