package com.beamofsoul.bip.management.security;

import static com.beamofsoul.bip.management.security.CustomUserDetailsService.convertToUserExtension;
import static com.beamofsoul.bip.management.util.UserUtils.saveCurrentUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.beamofsoul.bip.entity.dto.UserExtension;

public class CustomRememberMeServices extends TokenBasedRememberMeServices {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	public CustomRememberMeServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}
	
	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		super.onLoginSuccess(request, response, successfulAuthentication);
		SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
		this.afterOnLoginSuccess(request, response, successfulAuthentication);
	}
	
	private void afterOnLoginSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication successfulAuthentication) {
        saveSessionProperties(request, convertToUserExtension(customUserDetailsService.getUser0(successfulAuthentication.getName())));
    }
	
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
		this.afterProcessAutoLoginCookie(userDetails, request, response);
		return userDetails;
	}

	private void afterProcessAutoLoginCookie(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
		saveSessionProperties(request, convertToUserExtension(customUserDetailsService.getUser0(userDetails.getUsername())));
    }
	
	private void saveSessionProperties(HttpServletRequest request, UserExtension userExtension) {
		saveCurrentUser(request.getSession(), userExtension);
	}
}
