package com.beamofsoul.bip.management.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.entity.Role;
import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.service.UserService;

/**
 * @ClassName CustomUserDetailsService
 * @Description 自定义用户身份验证服务类
 * @author MingshuJian
 * @Date 2017年1月19日 下午4:28:32
 * @version 1.0.0
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	/*
	 * (非 Javadoc)  
	 * <p>Title: loadUserByUsername</p>  
	 * <p>Description: 获取用户信息后封装成UserExtension对象</p>  
	 * @param username
	 * @return UserExtension
	 * @throws UsernameNotFoundException  
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = getUser(username);
		return convertToUserExtension(user);
	}

	protected static com.beamofsoul.bip.entity.dto.UserExtension convertToUserExtension(User user) {
		com.beamofsoul.bip.entity.dto.UserExtension userExtension = new com.beamofsoul.bip.entity.dto.UserExtension(
                user.getId(), user.getUsername(), user.getPassword(), user.getNickname(),
                true,//是否可用
                true,//是否过期
                true,//证书不过期为true
                true,//账户未锁定为true
                getAuthorities(user));
		return userExtension;
	}

	/**
	 * @Title: getUser  
	 * @Description: 通过用户名判断对象是否存在并返回  
	 * @param @param username
	 * @param @return    参数  
	 * @return User    返回类型  
	 * @throws
	 */
	private User getUser(String username) {
		if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("用户名为空");
        User user = getUser0(username);
        if (user == null)
			throw new UsernameNotFoundException("用户不存在");
        if (user.getStatus() == 0)
        	throw new UsernameNotFoundException("用户已被锁定");
        if (user.getRoles() == null || user.getRoles().size() == 0)
        	throw new UsernameNotFoundException("用户暂未被分配角色");
		return user;
	}
	
	protected User getUser0(String username) {
		return userService.findByUsername(username);
	}

	protected static Set<GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (Role role : user.getRoles())
			authorities.add(new SimpleGrantedAuthority(
					"ROLE_" + role.getName().toUpperCase()));
		return authorities;
	}

}
