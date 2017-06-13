package com.beamofsoul.bip.management.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.beamofsoul.bip.entity.dto.UserExtension;
import com.beamofsoul.bip.management.util.UserUtils;

/**
 * @ClassName CurrentUserIdMethodArgumentResolver
 * @Description 自定义注解CurrentUserId作为方法参数时的解析器
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:52:08
 * @version 1.0.0
 */
public class CurrentUserIdMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(Long.class) && parameter.hasParameterAnnotation(CurrentUserId.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object userExtension = webRequest.getAttribute(UserUtils.CURRENT_USER, RequestAttributes.SCOPE_SESSION);
		return userExtension != null ? ((UserExtension) userExtension).getUserId() : null; 
	}
}
