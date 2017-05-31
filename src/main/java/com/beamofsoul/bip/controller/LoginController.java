package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.formatAndParseObject;
import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.service.LoginService;

@Controller
@RequestMapping("/admin/login")
public class LoginController extends BaseAbstractController {

	@Resource
	private LoginService loginService;
	
	@PreAuthorize("authenticated and hasPermission('login','login:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/login/admin_login_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('login','login:list')")
	@RequestMapping(value = "loginsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableLogins(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(loginService.findAll(pageable, 
				condition == null ? null : 
					loginService.onSearch(formatAndParseObject(condition.toString()))));
	}
}
