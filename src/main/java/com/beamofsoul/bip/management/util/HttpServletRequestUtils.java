package com.beamofsoul.bip.management.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ServletRequest;

/**
 * @ClassName HttpServletRequestUtils
 * @Description 为处理HttpServletRequest对象提供的工具类
 * @author MingshuJian
 * @Date 2017年6月2日 上午10:12:42
 * @version 1.0.0
 */
public class HttpServletRequestUtils {

	public static String getRequestBody(ServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		try (InputStream in = request.getInputStream();BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")))) {
			reader.lines().forEach(buffer::append);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
