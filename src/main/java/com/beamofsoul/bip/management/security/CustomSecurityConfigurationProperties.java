package com.beamofsoul.bip.management.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName CustomSecurityConfigurationProperties
 * @Description 自定义安全配置属性值读取器
 * @author MingshuJian
 * @Date 2017年1月19日 下午4:27:30
 * @version 1.0.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "project.base.security")
public class CustomSecurityConfigurationProperties {

	private String[] adminRoleMatchers;
	private String[] adminRoles;
	private String[] nonAuthenticatedMatchers;
	private String loginPage;
	private String defaultLoginSuccessUrl;
	private boolean alwaysUseDefaultSuccessUrl;
	private String logoutUrl;
	private String defaultLogoutSuccessUrl;
	private int maximumSessions;
	private boolean maxSessionsPreventsLogin;
	private String expiredUrl;
	private int tokenValiditySeconds;
	private String rememberMeParameter;
	private String rememberMeCookieName;
	private String[] ignoringMatchers;
}
