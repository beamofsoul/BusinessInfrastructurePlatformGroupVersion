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
import com.beamofsoul.bip.service.ActionMonitorService;

@Controller
@RequestMapping("/admin/actionMonitor")
public class ActionMonitorController extends BaseAbstractController {

	@Resource
	private ActionMonitorService actionMonitorService;
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/action_monitor/admin_action_monitor_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:list')")
	@RequestMapping(value = "actionMonitorsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableActionMonitors(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(actionMonitorService.findAll(pageable, 
				condition == null ? null : 
					actionMonitorService.onSearch(formatAndParseObject(condition.toString()))));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj",actionMonitorService.findById(Long.valueOf(map.get("id").toString())));
	}
}
