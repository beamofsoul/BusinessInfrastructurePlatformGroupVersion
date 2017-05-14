package com.beamofsoul.bip.management.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.collect.Lists;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomSecurityConfigurationProperties props;
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private TemplateResolver tmeplateResolver;
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(tmeplateResolver);
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}
	
	@Bean
	public TokenBasedRememberMeServices customRememberMeServices() {
		return new CustomRememberMeServices(props.getRememberMeCookieName(), customUserDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			.antMatchers(props.getAdminRoleMatchers()).hasAnyRole(props.getAdminRoles())
			.antMatchers(props.getNonAuthenticatedMatchers()).permitAll()
			.and().formLogin()
				.loginPage(props.getLoginPage()).permitAll().defaultSuccessUrl(props.getDefaultLoginSuccessUrl(), props.isAlwaysUseDefaultSuccessUrl()).successHandler(customAuthenticationSuccessHandler)
				.and().logout().logoutUrl(props.getLogoutUrl()).logoutSuccessUrl(props.getDefaultLogoutSuccessUrl())
				.and().sessionManagement().maximumSessions(props.getMaximumSessions())
				.maxSessionsPreventsLogin(props.isMaxSessionsPreventsLogin()).expiredUrl(props.getExpiredUrl())
				.and().and()
				.rememberMe()
					.tokenValiditySeconds(props.getTokenValiditySeconds())
					.rememberMeParameter(props.getRememberMeParameter())
					.rememberMeServices(customRememberMeServices());
        
		/**
		 * 增加响应头过滤器，当返回响应对象时设置允许JSP页面在<frame>、<iframe>或者<object>中展现
		 * X-Frame-Options参数的值有三种：
		 *  - DENY: 表示该页面不允许在 frame 中展示，即便是在相同域名的页面中嵌套也不允许(SpringSecurity默认值)
		 * 	- SAMEORIGIN: 表示该页面可以在相同域名页面的 frame 中展示(手动修改后的值)
		 * 	- ALLOW-FROM uri: 表示该页面可以在指定来源的 frame 中展示(暂时不会考虑使用的值)
		 * 
		 * PS: 其他的响应头参数可以替代JSP页面中设定的meta数据值
		 */
        HeaderWriterFilter responseFilter4Frame = new HeaderWriterFilter(Lists.newArrayList(new HeaderWriter() {
            @Override
        	public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {  
                response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");  
                response.setHeader("Expires", "0");  
                response.setHeader("Pragma", "no-cache");  
                response.setHeader("X-Frame-Options", "SAMEORIGIN");
                response.setHeader("X-UA-Compatible", "IE=edge");
                response.setHeader("X-XSS-Protection", "1; mode=block");
                response.setHeader("x-content-type-options", "nosniff");
            }
        }));
        http.addFilter(responseFilter4Frame);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(props.getIgnoringMatchers());
		
		/**
		 * 指定WebSecurity中使用自定义PermissionEvaluator
		 * 否则，JSP中<sec:authorize access="hasPermission('...','...')">将会失效
		 */
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setPermissionEvaluator(new CustomPermissionEvaluator());
		web.expressionHandler(handler);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}
}
