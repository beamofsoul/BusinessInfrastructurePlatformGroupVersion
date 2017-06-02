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
import com.beamofsoul.bip.entity.SensitiveWord;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.service.SensitiveWordService;

@Controller
@RequestMapping("/admin/sensitiveWord")
public class SensitiveWordController extends BaseAbstractController {

	@Resource
	private SensitiveWordService sensitiveWordService;
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/sensitive_word/admin_sensitive_word_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:add')")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody SensitiveWord sensitiveWord) {
		return newInstance("created",sensitiveWordService.create(sensitiveWord));
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:list')")
	@RequestMapping(value = "sensitiveWordsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableSensitiveWords(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(sensitiveWordService.findAll(pageable, 
				condition == null ? null : 
					sensitiveWordService.onSearch(formatAndParseObject(condition.toString()))));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj",sensitiveWordService.findById(Long.valueOf(map.get("id").toString())));
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:update')")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody SensitiveWord sensitiveWord) {
		return newInstance("updated",sensitiveWordService.update(sensitiveWord));
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:delete')")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",sensitiveWordService
				.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:list')")
	@RequestMapping(value = "/getOpen", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject setSensitiveFilterOpen() {
		return newInstance("open", sensitiveWordService.findSensitiveFilterOpen());
	}
	
	@PreAuthorize("authenticated and hasPermission('sensitiveWord','sensitiveWord:list')")
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	@ResponseBody //$
	public JSONObject setSensitiveFilterOpen(@RequestBody Map<String, Object> map) {
		sensitiveWordService.setSensitiveFilterOpen(Boolean.valueOf(map.get("isOpen").toString()));
		return newInstance("null","null"); //$ 无意义，thymeleaf如果返回void, 则默认将请求连接作为相应连接
	}
}
