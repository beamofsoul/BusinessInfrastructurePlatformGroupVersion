package com.beamofsoul.bip.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableConfigurationProperties({ ServerProperties.class })
public class ExceptionController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	private ErrorAttributes errorAttributes;
	
	@Autowired
	private ServerProperties serverProperties;
	
	@Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

	@RequestMapping(produces = "text/html", value = ERROR_PATH)
	public ModelAndView handleHtmlError(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setStatus(getStatus(request).value());
		Map<String, Object> model = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		return new ModelAndView(ERROR_PATH, model);
	}

	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleJsonError(
			HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.TEXT_HTML));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}

	/**
	 * 确定是否应该包含堆栈中trace属性的值
	 */
	protected boolean isIncludeStackTrace(HttpServletRequest request,
			MediaType produces) {
		ErrorProperties.IncludeStacktrace include = this.serverProperties
				.getError().getIncludeStacktrace();
		if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}

	/**
	 * 获取错误的信息
	 */
	private Map<String, Object> getErrorAttributes(HttpServletRequest request,
			boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);
	}

	/**
	 * 是否包含trace
	 */
	private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		if (parameter == null) {
			return false;
		}
		return !"false".equals(parameter.toLowerCase());
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		if (statusCode != null) {
			try {
				return HttpStatus.valueOf(statusCode);
			} catch (Exception e) {}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
