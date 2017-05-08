package com.beamofsoul.bip.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.entity.dto.UserExtension;

/**
 * @ClassName CustomAuthenticationProvider
 * @Description 自定义身份验证提供器
 * @author MingshuJian
 * @Date 2017年1月19日 下午4:18:21
 * @version 1.0.0
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	/*
	 * (非 Javadoc)  
	 * <p>Title: authenticate</p>  
	 * <p>Description: 根据用户名读取用户身份信息，并提供简单的密码匹配验证</p>  
	 * @param authentication
	 * @return UsernamePasswordAuthenticationToken
	 * @throws AuthenticationException  
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails user = customUserDetailsService
				.loadUserByUsername(authentication.getName());
		
		//此处可以增加加密验证或验证码验证等功能
		//先判断是否是remember-me登录
		if (authentication.getPrincipal() instanceof UserExtension) {
			if (!((UserExtension) authentication.getPrincipal()).getPassword().equals(user.getPassword()))
				throw new UsernameNotFoundException("密码错误");
		} else if (!authentication.getCredentials().equals(user.getPassword())) {
			throw new UsernameNotFoundException("密码错误");
		}

		UsernamePasswordAuthenticationToken result = 
				new UsernamePasswordAuthenticationToken(
				user.getUsername(), 
				user.getPassword(),
				user.getAuthorities());
		result.setDetails(user);
		return result;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
