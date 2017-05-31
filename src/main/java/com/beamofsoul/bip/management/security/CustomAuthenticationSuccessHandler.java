package com.beamofsoul.bip.management.security;

import static com.beamofsoul.bip.management.util.UserUtils.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.entity.Login;
import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.entity.dto.UserExtension;
import com.beamofsoul.bip.management.util.ClientInformationUtils;
import com.beamofsoul.bip.service.LoginService;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private LoginService logService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		/**
		 * 正常登录(未勾选RememberMe)需要在此装载自定义Session属性值
		 * RememberMe登录由于在CustomRememberMeSerivces中装载了自定义Session属性值，所以无需额外操作
		 */
		if (!isExist(request.getSession())) {
			UserExtension ux = (UserExtension) authentication.getDetails();			
			saveCurrentUser(request.getSession(), ux);
			request.getSession().setAttribute("currentLogin", new LoginRecordHandler().saveLoginRecord(new User(ux.getUserId()), request, response));
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	public class LoginRecordHandler{
		
		public Login saveLoginRecord(User currentUser, HttpServletRequest request, HttpServletResponse response) {
			Login login = new Login();
	        login.setUser(currentUser);
	        
	        try {
				login.setIpAddress(ClientInformationUtils.getIpAddress(request));
				login.setOperatingSystem(ClientInformationUtils.getOperatingSystem(request));
		        login.setBrowser(ClientInformationUtils.getBrowser(request));
		        login.setBrand(ClientInformationUtils.getBrand(request));
		        login.setModel(ClientInformationUtils.getModel(request));
		        login.setScreenSize(ClientInformationUtils.getScreenSize(request));
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        return logService.create(login);
		}
	}
}
