package com.beamofsoul.bip.management.control;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.beamofsoul.bip.management.util.HttpServletRequestUtils;

import lombok.Getter;

/**
 * @ClassName CostomHttpServletRequestWrapper
 * @Description 自定义HttpServlet请求包装器
 * @author MingshuJian
 * @Date 2017年6月2日 上午10:11:17
 * @version 1.0.0
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

	@Getter
	private byte[] requestBody;
	public final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	public CustomHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		requestBody = HttpServletRequestUtils.getRequestBody(request).getBytes(DEFAULT_CHARSET);
	}
	
	public CustomHttpServletRequestWrapper(HttpServletRequest request, String requestBody) {
		super(request);
		this.setRequestBody(requestBody);
	}
	
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody.getBytes(DEFAULT_CHARSET);
	}
	
	public void setRequestBody(byte[] requestBody) {
		this.requestBody = requestBody;
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// do nothing...
			}

			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}
}
