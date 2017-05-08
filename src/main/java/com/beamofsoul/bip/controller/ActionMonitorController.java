package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.ActionMonitor;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
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
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:add')")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody ActionMonitor actionMonitor) {
		return newInstance("created",actionMonitorService.create(actionMonitor));
	}
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:list')")
	@RequestMapping(value = "actionMonitorsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableActionMonitors(@RequestBody Map<String, Object> map) {
		return newInstance(actionMonitorService.findAll(PageUtils.parsePageable(map)));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj",actionMonitorService.findById(Long.valueOf(map.get("id").toString())));
	}
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:update')")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody ActionMonitor actionMonitor) {
		return newInstance("updated",actionMonitorService.update(actionMonitor));
	}
	
	@PreAuthorize("authenticated and hasPermission('actionMonitor','actionMonitor:delete')")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",actionMonitorService
				.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
}
