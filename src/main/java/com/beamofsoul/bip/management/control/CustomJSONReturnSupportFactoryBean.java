package com.beamofsoul.bip.management.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CustomJSONReturnSupportFactoryBean
 * @Description 在RequestResponseBodyMethodProcessor前加入自定义的JSON返回值控制器以支持{@code @JSON}与{@code @JSONS}注解
 * @author MingshuJian
 * @Date 2017年2月17日 上午10:30:46
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class CustomJSONReturnSupportFactoryBean implements InitializingBean {

	@Autowired
	private RequestMappingHandlerAdapter adapter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
		adapter.setReturnValueHandlers(decorateHandlers(returnValueHandlers));
	}
	
	private List<HandlerMethodReturnValueHandler> decorateHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>();
		for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
            	CustomJSONReturnHandler decorator = new CustomJSONReturnHandler(handler);
                newReturnValueHandlers.add(decorator);
                log.info("自定义JSON返回值控制器[CustomJSONReturnHandler]注册完毕...");
            }
            newReturnValueHandlers.add(handler);
        }
		return newReturnValueHandlers;
	}
}
