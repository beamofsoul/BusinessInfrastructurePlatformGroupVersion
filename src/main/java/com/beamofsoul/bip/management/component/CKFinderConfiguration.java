package com.beamofsoul.bip.management.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ckfinder.connector.ConnectorServlet;

@Configuration
public class CKFinderConfiguration {

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servletRegistrationBean = 
				new ServletRegistrationBean(
						new ConnectorServlet(), 
								"/static/ckfinder/core/connector/java/connector.java");
		Map<String,String> initParameters = new HashMap<>();
		initParameters.put("XMLConfig", "/WEB-INF/ckfinder.xml");
		initParameters.put("debug", "false");
		servletRegistrationBean.setInitParameters(initParameters);
		servletRegistrationBean.setLoadOnStartup(1);
		return servletRegistrationBean;
	}
}
