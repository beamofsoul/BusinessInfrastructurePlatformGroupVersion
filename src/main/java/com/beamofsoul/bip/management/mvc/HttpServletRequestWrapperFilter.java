package com.beamofsoul.bip.management.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.beamofsoul.bip.management.control.CustomHttpServletRequestWrapper;
import com.beamofsoul.bip.management.util.HttpServletRequestUtils;

/**
 * @ClassName HttpServletRequestWrapperFilter
 * @Description 解决HttpServletRequest中body输入流只能读取一次的问题
 * @author MingshuJian
 * @Date 2017年6月13日 上午8:54:52
 * @version 1.0.0
 */
@WebFilter(filterName = "httpServletRequestWrapperFilter", urlPatterns = "/*")
public class HttpServletRequestWrapperFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		CustomHttpServletRequestWrapper requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			String requestBody = HttpServletRequestUtils.getRequestBody(httpServletRequest);
			requestWrapper = new CustomHttpServletRequestWrapper(httpServletRequest,requestBody);
		}
		chain.doFilter(requestWrapper == null ? request : requestWrapper, response);
	}

	@Override
	public void destroy() {
		
	}
}
