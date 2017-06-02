package com.beamofsoul.bip.management.security;

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
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

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
