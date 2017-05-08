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
}
