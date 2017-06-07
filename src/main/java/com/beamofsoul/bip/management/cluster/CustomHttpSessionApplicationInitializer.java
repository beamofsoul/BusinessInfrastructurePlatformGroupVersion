//package com.beamofsoul.bip.management.cluster;
//
//import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
//
//import com.beamofsoul.bip.management.security.CustomWebSecurityConfig;
//
///**
// * @ClassName CustomHttpSessionApplicationInitializer
// * @Description This ensures that the Spring Bean by the name springSessionRepositoryFilter is registered with our Servlet Container for every request before Spring Security’s springSecurityFilterChain
// * @author MingshuJian
// * @Date 2017年6月7日 上午10:08:30
// * @version 1.0.0
// */
//public class CustomHttpSessionApplicationInitializer extends AbstractHttpSessionApplicationInitializer {
//
//	public CustomHttpSessionApplicationInitializer() {
//		//确保Spring加载了安全框架的配置和自定义HttpSession的配置
//		super(CustomWebSecurityConfig.class, HttpSessionConfiguration.class);
//	}
//}
