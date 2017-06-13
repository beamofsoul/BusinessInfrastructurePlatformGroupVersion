package com.beamofsoul.bip.management.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.management.control.CustomHttpServletRequestWrapper;
import com.beamofsoul.bip.management.util.PageUtils;

/**
 * @ClassName PageableMethodArgumentResolver
 * @Description 自定义注解PageableAttribute作为方法参数时的解析器
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:52:08
 * @version 1.0.0
 */
public class PageableMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(Pageable.class) && parameter.hasParameterAnnotation(PageableAttribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request instanceof CustomHttpServletRequestWrapper) {
			String requestBody = new String(((CustomHttpServletRequestWrapper) request).getRequestBody());
			JSONObject attributes = JSONObject.parseObject(requestBody);
			if (attributes.containsKey(PageUtils.PAGEABLE_PAGE_SIZE_NAME) && attributes.containsKey(PageUtils.PAGEABLE_PAGE_NUMBER_NAME))
				return PageUtils.parsePageable((Map<String, Object>) attributes);
		}
		return null;
	}
}
