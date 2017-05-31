package com.beamofsoul.bip.management.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ClientInformationUtils
 * @Description 客户端相关信息工具类
 * @author MingshuJian
 * @Date 2017年4月12日 上午9:06:09
 * @version 1.0.0
 */
@Slf4j
public class ClientInformationUtils {
	
	public static final String CLIENT_IP_ADDRESS = "clientIp";
	public static final String CLIENT_MAC_ADDRESS = "clientMac";

	public static String getIpAddress(HttpServletRequest request) throws Exception {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					log.debug("获取本机IP地址失败", e);
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public static String getMacAddress(String ipAddress) throws Exception {
		ClientMacAddressHandler handler = new ClientMacAddressHandler(ipAddress);
		return handler.getRemoteMacAddress();
	}
	
	public static String getOperatingSystem(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String lowerUserAgent = userAgent.toLowerCase();
		String os = "";
		if (lowerUserAgent.contains("windows")) {
			os = "Windows";
		} else if (lowerUserAgent.contains("mac")) {
			os = "Mac";
		} else if (lowerUserAgent.contains("x11")) {
			os = "Unix";
		} else if (lowerUserAgent.contains("android")) {
			os = "Android";
		} else if (lowerUserAgent.contains("iphone")) {
			os = "IOS";
		} else {
			os = "Others, More-Info: " + userAgent;
		}
		return os;
	}
	
	public static String getBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String lowerUserAgent = userAgent.toLowerCase();
		String browser = "";
		if (lowerUserAgent.contains("edge")) {
			browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
		} else if (lowerUserAgent.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
		} else if (lowerUserAgent.contains("safari") && lowerUserAgent.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+ "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (lowerUserAgent.contains("opr") || lowerUserAgent.contains("opera")) {
			if(lowerUserAgent.contains("opera")){  
                browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];  
            }else if(lowerUserAgent.contains("opr")){  
                browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");  
            }  
		} else if (lowerUserAgent.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((lowerUserAgent.indexOf("mozilla/7.0") > -1) || (lowerUserAgent.indexOf("netscape6") != -1)  ||  
                (lowerUserAgent.indexOf("mozilla/4.7") != -1) || (lowerUserAgent.indexOf("mozilla/4.78") != -1) ||  
                (lowerUserAgent.indexOf("mozilla/4.08") != -1) || (lowerUserAgent.indexOf("mozilla/3") != -1)) {  
			browser = "Netscape-?";
		} else if (lowerUserAgent.contains("firefox")) {  
            browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");  
        } else if(lowerUserAgent.contains("rv")) {  
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");  
            browser = "IE" + IEVersion.substring(0,IEVersion.length() - 1);  
        } else {  
            browser = "Others, More-Info: " + userAgent;  
        }
		
		return browser;
	}
	
	public static String getBrand(HttpServletRequest request) {
		return request.getHeader("clientBrand");
	}
	
	public static String getModel(HttpServletRequest request) {
		return request.getHeader("clientModel");
	}
	
	public static String getScreenSize(HttpServletRequest request) {
		return request.getHeader("clientScreenSize");
	}
}
