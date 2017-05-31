package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.formatAndParseObject;
import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.management.util.Constants;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.management.util.UserUtils;
import com.beamofsoul.bip.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseAbstractController {

	@Resource
	private UserService userService;
	
	@PreAuthorize("authenticated and hasPermission('user','user:list')")
	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/user/admin_user_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('user','user:add')")
	@RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSingle(@RequestBody User user) {
		return newInstance("created",userService.create(user));
	}
	
	@PreAuthorize("authenticated and hasPermission('user','user:list')")
	@RequestMapping(value = "usersByPage", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getPageableUsers(@RequestBody Map<String, Object> map) {
		
		Object condition = map.get("condition");
		Pageable pageable = PageUtils.parsePageable(map);
		return newInstance(userService.findAll(pageable, 
				condition == null ? null : 
					userService.onSearch(formatAndParseObject(condition.toString()))));
	}
	
	@RequestMapping(value = "single", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
		return newInstance("obj", userService.findById(Long.valueOf(map.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY).toString())));
	}
	
	@PreAuthorize("authenticated and hasPermission('user','user:update')")
	@RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateSingle(@RequestBody User user) {
		return newInstance("updated",userService.update(user));
	}
	
	@PreAuthorize("authenticated and hasPermission('user','user:delete')")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public JSONObject delete(@RequestBody String ids) {
		return newInstance("count",userService.delete(CommonConvertUtils.convertToLongArray(ids)));
	}
	
	@RequestMapping(value = "/checkUsernameUnique", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkUsernameUnique(@RequestBody Map<String, Object> map) {
		String username = map.get("data").toString();
		Long userId = map.containsKey(Constants.DEFAULT_ENTITY_PRIMARY_KEY) ? Long.valueOf(map.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY).toString()) : null;
		return newInstance("isUnique", userService.checkUsernameUnique(username, userId));
	}
	
	@RequestMapping(value = "/checkNicknameUnique", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkNicknameUnique(@RequestBody Map<String, Object> map) {
		String nickname = map.get("data").toString();
		Long userId = map.containsKey(Constants.DEFAULT_ENTITY_PRIMARY_KEY) ? Long.valueOf(map.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY).toString()) : null;
		return newInstance("isUnique", userService.checkNicknameUnique(nickname, userId));
	}
	
	@RequestMapping(value = "/isUsed", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject isUsed(@RequestBody String objectIds) {
		return newInstance("isUsed", userService.isUsed(objectIds));
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changePassword(@RequestBody Map<String, String> map, HttpSession session) {
		boolean changed = userService.changePassword(UserUtils.getLongUserId(session), map.get("currentPassword"), map.get("latestPassword"));
		if (changed) UserUtils.getTraditionalUser(session).setPassword(map.get("latestPassword"));
		return newInstance("changed", changed);
	}
	
	@RequestMapping(value = "/updatePhoto", method = RequestMethod.POST)
	public void updatePhoto(@RequestParam("file") MultipartFile[] files) {
		MultipartFile file = files[0];
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
	}
}