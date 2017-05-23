package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.formatAndParseObject;
import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.math.BigDecimal;
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
import com.beamofsoul.bip.entity.Department;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.service.DepartmentService;

@Controller
@RequestMapping("/admin/department")
public class DepartmentController extends BaseAbstractController {

	@Resource
	private DepartmentService departmentService;
	
	@PreAuthorize("authenticated and hasPermission('department','department:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/department/admin_department_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('department','department:add')")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody Department department) {
		Department create = departmentService.create(department);
		return newInstance("created",create);
	}
	
	@PreAuthorize("authenticated and hasPermission('department','department:list')")
	@RequestMapping(value = "departmentsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableDepartments(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(departmentService.findAll(pageable, 
				condition == null ? null : 
					departmentService.onSearch(formatAndParseObject(condition.toString()))));
//		return newInstance(departmentService.findAll(PageUtils.parsePageable(map)));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj",departmentService.findById(Long.valueOf(map.get("id").toString())));
	}
	
	@PreAuthorize("authenticated and hasPermission('department','department:update')")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody Department department) {
		return newInstance("updated",departmentService.update(department));
	}
	
	@PreAuthorize("authenticated and hasPermission('department','department:delete')")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",departmentService
				.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
	
	@RequestMapping(value = "/checkDepartmentCodeUnique", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkDepartmentCodeUnique(@RequestBody Map<String, Object> map) {
		String code = map.get("data").toString();
		Long id = map.containsKey("id") ? Long.valueOf(map.get("id").toString()) : null;
		return newInstance("isUnique", departmentService.checkDepartmentCodeUnique(code, id));
	}
	
	@RequestMapping(value = "/getAllAvailableDepartments", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getAllAvailableDepartments() {
		return newInstance("parents", departmentService.findAllAvailableDepartments());
	}
	
	@RequestMapping(value = "/getChildrenIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getChildrenIds(@RequestBody Map<String, Object> map) {
		return newInstance("ids", departmentService.findChildrenIds(new BigDecimal(map.get("id").toString()).longValue()));
	}
	
	@PreAuthorize("authenticated and hasPermission('department','department:list')")
	@RequestMapping(value = "children", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getChildrenData(@RequestBody Map<String, Object> map) {
		Object condition = map.get("condition");
		return newInstance("children", departmentService.findRelationalAll(condition == null ? null : 
			departmentService.onRelationalSearch(formatAndParseObject(condition.toString()))));
	}
}
