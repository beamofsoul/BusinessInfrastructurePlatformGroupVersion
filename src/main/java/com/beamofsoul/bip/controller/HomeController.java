package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.management.util.Constants;
import com.beamofsoul.bip.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/admin/adminIndex", method = RequestMethod.GET)
	public ModelAndView adminIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("admin_index_iview");
	}
	
	@RequestMapping(value = "/admin/adminIndexContent", method = RequestMethod.GET)
	public ModelAndView adminIndexContent() {
		return new ModelAndView("/backend/admin_backend_index_content_iview");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpSession session, Map<String, Object> map) {
		if (session.getAttribute(Constants.CURRENT_USER) != null)
			return "redirect:/index";
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session, Map<String, Object> map) {
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public String expired(Map<String, Object> map) {
		map.put("expired", "该账号已经在其他地方重新登录");
		return "login";
	}
	
	@RequestMapping(value = "/anonymous", method = RequestMethod.GET)
	public String anonymousLogin() {
		return "admin_index";
	}
	
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject forgetPassword(@RequestBody Map<String, String> map) {
		return newInstance("message", userService.forgotPassword(map.get("username")));
	}

//	@RequestMapping(value = "/admin/systemLog")
//	public String systemLog() {
//		return "/system_log/admin_system_log_list";
//	}
}
