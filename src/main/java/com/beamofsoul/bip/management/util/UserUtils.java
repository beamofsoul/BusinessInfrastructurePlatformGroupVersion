package com.beamofsoul.bip.management.util;

import javax.servlet.http.HttpSession;

import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.entity.dto.UserExtension;

public class UserUtils {

	public static final String CURRENT_USER = "CURRENT_USER";
	
	public static void saveCurrentUser(HttpSession session, UserExtension userExtension) {
		session.setAttribute(CURRENT_USER, userExtension);
	}
	
	public static UserExtension getCurrentUser(HttpSession session) {
		return (UserExtension) session.getAttribute(CURRENT_USER);
	}
	
	public static boolean isExist(HttpSession session) {
		return getCurrentUser(session) != null;
	}
	
	public static long getLongUserId(HttpSession session) {
		return getCurrentUser(session).getUserId();
	}
	
	public static String getStringUserId(HttpSession session) {
		return String.valueOf(getLongUserId(session));
	}
	
	public static User getTraditionalUser(HttpSession session) {
		return new User(getLongUserId(session));
	}
	
	public static void trySavingCurrentUser(HttpSession session, Object user) {
		if (user instanceof UserExtension) saveCurrentUser(session, (UserExtension)user);
	}

}
