package com.beamofsoul.bip.management.mvc;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName CustomWebMvcConfiguration
 * @Description 自定义 Web MVC 配置适配器，用以加载自定义方法参数解析器等自定义配置
 * @author MingshuJian
 * @Date 2017年6月12日 下午3:02:33
 * @version 1.0.0
 */
@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserMethodArgumentResolver());
		argumentResolvers.add(currentUserIdMethodArgumentResolver());
		argumentResolvers.add(pageableMethodArgumentResolver());
		argumentResolvers.add(conditionMethodArgumentResolver());
		argumentResolvers.add(idMethodArgumentResolver());
		argumentResolvers.add(settingAttributeMethodArgumentResolver());
		super.addArgumentResolvers(argumentResolvers);
	}
	
	@Bean
	public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
		return new CurrentUserMethodArgumentResolver();
	}
	
	@Bean
	public CurrentUserIdMethodArgumentResolver currentUserIdMethodArgumentResolver() {
		return new CurrentUserIdMethodArgumentResolver();
	}
	
	@Bean
	public PageableMethodArgumentResolver pageableMethodArgumentResolver() {
		return new PageableMethodArgumentResolver();
	}
	
	@Bean
	public ConditionMethodArgumentResolver conditionMethodArgumentResolver() {
		return new ConditionMethodArgumentResolver();
	}
	
	@Bean
	public IdMethodArgumentResolver idMethodArgumentResolver() {
		return new IdMethodArgumentResolver();
	}
	
	@Bean
	public SettingAttributeMethodArgumentResolver settingAttributeMethodArgumentResolver() {
		return new SettingAttributeMethodArgumentResolver();
	}
}
