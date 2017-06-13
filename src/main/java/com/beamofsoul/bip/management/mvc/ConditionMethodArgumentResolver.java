package com.beamofsoul.bip.management.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.management.control.CustomHttpServletRequestWrapper;
import com.beamofsoul.bip.management.util.JSONUtils;

/**
 * @ClassName ConditionMethodArgumentResolver
 * @Description 自定义注解ConditionAttribute作为方法参数时的解析器
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:52:08
 * @version 1.0.0
 */
public class ConditionMethodArgumentResolver implements HandlerMethodArgumentResolver{

	private static final String CONDITION = "condition";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//即使当前参数需要返回的类型是JSONObject或任意实现了Map接口的对象类型，仍然只能返回其他非Map关联类型，包括JSONObject
		//Spring MVC在解析action输入参数时，会将非@RequestBody注解标识的实现了Map接口的对象类型映射为org.springframework.validation.support.BindingAwareModelMap
		//所以如果action方法签名中定义了非@RequestBody注解标识的JSONObject或Map类型对象，则会报错: java.lang.IllegalArgumentException: argument type mismatch
		return parameter.getParameterType().isAssignableFrom(Object.class) && parameter.hasParameterAnnotation(ConditionAttribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request instanceof CustomHttpServletRequestWrapper) {
			String requestBody = new String(((CustomHttpServletRequestWrapper) request).getRequestBody());
			JSONObject attributes = JSONObject.parseObject(requestBody);
			if (attributes.containsKey(CONDITION))
				return JSONUtils.formatAndParseObject(attributes.get(CONDITION).toString());
		}
		return null;
	}
}
