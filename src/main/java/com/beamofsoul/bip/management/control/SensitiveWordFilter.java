package com.beamofsoul.bip.management.control;

import static com.beamofsoul.bip.management.util.ConfigurationReader.PROJECT_BASE_FILTER_SENSITIVE;
import static com.beamofsoul.bip.management.util.ConfigurationReader.asBoolean;
import static com.beamofsoul.bip.management.util.ConfigurationReader.getValue;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beamofsoul.bip.management.util.HttpServletRequestUtils;
import com.beamofsoul.bip.management.util.SensitiveWordsMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SensitiveWordFilter
 * @Description 敏感词过滤器(标记WebFilter注解的类会被ServletComponentScan注解扫描识别)
 * @author MingshuJian
 * @Date 2017年6月2日 上午10:02:44
 * @version 1.0.0
 */
@Slf4j
@WebFilter(filterName = "sensitiveWordFilter", urlPatterns = "/*")
public class SensitiveWordFilter implements Filter {

	public static boolean isOpen;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		isOpen = asBoolean(getValue(PROJECT_BASE_FILTER_SENSITIVE));
		log.debug("初始化敏感词过滤器完毕，当前状态为:" + (isOpen ? "开启" : "关闭") + "...");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if (isOpen && httpServletRequest.getMethod().equalsIgnoreCase(RequestMethod.POST.name())) {
			String requestBody = HttpServletRequestUtils.getRequestBody(httpServletRequest);
			CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpServletRequest,requestBody);
			if (StringUtils.isNotBlank(requestBody)) requestWrapper.setRequestBody(SensitiveWordsMapping.filter(requestBody));
			chain.doFilter(requestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		log.debug("销毁敏感词过滤器完毕...");
	}
}
