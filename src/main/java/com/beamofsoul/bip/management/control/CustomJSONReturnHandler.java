package com.beamofsoul.bip.management.control;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName JsonReturnHandler
 * @Description 对Controller中方法返回的JSON对象进行解析、控制和过滤
 * @author MingshuJian
 * @Date 2017年2月16日 下午4:18:42
 * @version 1.0.0
 */
public class CustomJSONReturnHandler implements HandlerMethodReturnValueHandler {
	
	//RequestResponseBodyMethodProcessor
	private final HandlerMethodReturnValueHandler delegate;
	
	public CustomJSONReturnHandler(HandlerMethodReturnValueHandler delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		//如果Controller的方法上有@JSON注解，则使用自定义的Handler进行处理
		return returnType.hasMethodAnnotation(JSON.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		//设置当前handler不是对返回值的最终的处理类，如需设置为最终处理类需设置成true
		//处理完再去找下一个类(RequestResponseBodyMethodProcessor)进行处理
		mavContainer.setRequestHandled(false);
		
		//如果返回值为null，则不需要处理序列化等问题
		if (returnValue == null) return;

		//解析注解中配置的参数
		Annotation[] annotations = returnType.getMethodAnnotations();
		CustomJSONSerializer serializer = new CustomJSONSerializer();
		Arrays.asList(annotations).forEach(e -> { 
			if (e instanceof JSON) {
				serializer.filter((JSON) e);
			} else if (e instanceof JSONS) {
				Arrays.asList(((JSONS) e).value()).forEach(serializer::filter);
			}
		});
		
		//根据注解参数对返回值进行序列化、过滤与解析
		JSONObject returnJSON = (JSONObject) returnValue;
		JSONObject parsedReturnValue = new JSONObject();
		for (String key : returnJSON.keySet()) {
			parsedReturnValue.put(key, JSONObject.parse(serializer.toJSON(returnJSON.get(key))));
		}
		returnValue = serializer.toJSON(parsedReturnValue);
		//当前控制器处理过后，交由RequestResponseBodyMethodProcessor进行最终处理
		delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
		
		//如果不需要交由其他处理器处理，可直接使用下面代码直接进行对请求的响应
//		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//      response.getWriter().write(serializer.toJson(returnValue));
	}
}
