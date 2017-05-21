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
import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.service.OrganizationService;

@Controller
@RequestMapping("/admin/organization")
public class OrganizationController extends BaseAbstractController {

	@Resource
	private OrganizationService organizationService;
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/organization/admin_organization_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:add')")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody Organization organization) {
		return newInstance("created",organizationService.create(organization));
	}
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:list')")
	@RequestMapping(value = "organizationsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableOrganizations(@RequestBody Map<String, Object> map) {
//		return newInstance(organizationService.findAll(PageUtils.parsePageable(map)));
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(organizationService.findAll(pageable, 
				condition == null ? null : 
					organizationService.onSearch(formatAndParseObject(condition.toString()))));
	}
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:list')")
	@RequestMapping(value = "children", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getChildrenData(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		return newInstance("children",organizationService.findRelationalAll(condition == null ? null : 
			organizationService.onRelationalSearch(formatAndParseObject(condition.toString()))));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj",organizationService.findById(Long.valueOf(map.get("id").toString())));
	}
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:update')")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody Organization organization) {
		return newInstance("updated",organizationService.update(organization));
	}
	
	@PreAuthorize("authenticated and hasPermission('organization','organization:delete')")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",organizationService
				.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
}
