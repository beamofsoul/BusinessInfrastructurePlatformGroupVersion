package com.beamofsoul.bip.management.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.entity.dto.UserExtension;
import com.beamofsoul.bip.management.util.UserUtils;
import com.beamofsoul.bip.service.UserService;

/**
 * @ClassName CurrentUserMethodArgumentResolver
 * @Description 自定义注解CurrentUser作为方法参数时的解析器
 * @author MingshuJian
 * @Date 2017年6月12日 下午2:52:08
 * @version 1.0.0
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Autowired
	private UserService userService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(User.class) && parameter.hasParameterAnnotation(CurrentUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object userExtension = webRequest.getAttribute(UserUtils.CURRENT_USER, RequestAttributes.SCOPE_SESSION);
		return userExtension != null ? userService.findById(((UserExtension) userExtension).getUserId()) : null; 
	}
}
