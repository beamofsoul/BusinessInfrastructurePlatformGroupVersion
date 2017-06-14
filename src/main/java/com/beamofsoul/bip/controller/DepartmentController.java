package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Department;
import com.beamofsoul.bip.management.mvc.Attribute;
import com.beamofsoul.bip.management.mvc.ConditionAttribute;
import com.beamofsoul.bip.management.mvc.IdAttribute;
import com.beamofsoul.bip.management.mvc.PageableAttribute;
import com.beamofsoul.bip.management.security.Authorize;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.service.DepartmentService;

@Controller
@RequestMapping("/admin/department")
public class DepartmentController extends BaseAbstractController {

	@Resource
	private DepartmentService departmentService;
	
	@Authorize("department:list")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/department/admin_department_list";
	}
	
	@Authorize("department:add")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody Department department) {
		return newInstance("created", departmentService.create(department));
	}
	
	@Authorize("department:list")
	@RequestMapping(value = "departmentsByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getPageableDepartments(@RequestBody Map<String, Object> map, @PageableAttribute Pageable pageable, @ConditionAttribute Object condition) {
		return newInstance(departmentService.findAll(pageable, departmentService.onSearch((JSONObject) condition)));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map, @IdAttribute Long id) {
		return newInstance("obj",departmentService.findById(id));
	}
	
	@Authorize("department:update")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody Department department) {
		return newInstance("updated",departmentService.update(department));
	}
	
	@Authorize("department:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",departmentService
				.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
	
	@RequestMapping(value = "/checkDepartmentCodeUnique", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkDepartmentCodeUnique(@RequestBody Map<String, Object> map, @Attribute("data") String code, @IdAttribute Long id) {
		return newInstance("isUnique", departmentService.checkDepartmentCodeUnique(code, id));
	}
	
	@RequestMapping(value = "/getAllAvailableDepartments", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getAllAvailableDepartments() {
		return newInstance("parents", departmentService.findAllAvailableDepartments());
	}
	
	@RequestMapping(value = "/getChildrenIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getChildrenIds(@RequestBody Map<String, Object> map, @IdAttribute Long id) {
		return newInstance("ids", departmentService.findChildrenIds(id));
	}
	
	@Authorize("department:list")
	@RequestMapping(value = "children", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
	@ResponseBody
	public JSONObject getChildrenData(@RequestBody Map<String, Object> map, @ConditionAttribute Object condition) {
		return newInstance("children", departmentService.findRelationalAll(condition == null ? null : 
			departmentService.onRelationalSearch((JSONObject) condition)));
	}
}
