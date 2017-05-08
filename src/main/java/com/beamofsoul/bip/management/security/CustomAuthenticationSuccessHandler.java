package com.beamofsoul.bip.management.security;

import static com.beamofsoul.bip.management.util.UserUtils.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.entity.dto.UserExtension;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		/**
		 * 正常登录(未勾选RememberMe)需要在此装载自定义Session属性值
		 * RememberMe登录由于在CustomRememberMeSerivces中装载了自定义Session属性值，所以无需额外操作
		 */
		if (!isExist(request.getSession()))
			saveCurrentUser(request.getSession(), (UserExtension) authentication.getDetails());
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
