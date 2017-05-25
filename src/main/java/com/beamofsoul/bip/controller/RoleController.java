package com.beamofsoul.bip.controller;

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
import com.beamofsoul.bip.entity.Role;
import com.beamofsoul.bip.entity.UserRoleCombineRole;
import com.beamofsoul.bip.entity.dto.RolePermissionMappingDTO;
import com.beamofsoul.bip.management.control.JSON;
import com.beamofsoul.bip.management.util.CommonConvertUtils;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.service.RolePermissionService;
import com.beamofsoul.bip.service.RoleService;
import com.beamofsoul.bip.service.UserRoleService;

@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseAbstractController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleSerivce;

    @Resource
    private RolePermissionService rolePermissionService;

    @PreAuthorize("authenticated and hasPermission('role','role:list')")
    @RequestMapping(value = "/adminList")
    public String adminList() {
        return "/role/admin_role_list";
    }

    @PreAuthorize("authenticated and hasPermission('role','role:roleuser')")
    @RequestMapping(value = "/adminListWithUsers")
    public String adminListWithUsers() {
        return "/role_user/admin_role_user_list";
    }

    @PreAuthorize("authenticated and hasPermission('role','role:add')")
    @RequestMapping(value = "/add")
    public String add() {
        return "/role/admin_role_add";
    }

    @PreAuthorize("authenticated and hasPermission('role','role:add')")
    @RequestMapping(value = "/singleAdd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addSingle(@RequestBody Role role) {
        return newInstance("created", roleService.create(role));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:list')")
    @RequestMapping(value = "rolesByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
    @ResponseBody
    public JSONObject getPageableData(@RequestBody Map<String, Object> map) {
        return newInstance(roleService.findAll(PageUtils.parsePageable(map)));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:roleuser')")
    @RequestMapping(value = "userRolesByPage", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
    @ResponseBody
    public JSONObject getPageableUserRolesData(@RequestBody Map<String, Object> map) {
        Object condition = map.get("condition");
        Pageable pageable = PageUtils.parsePageable(map);
        return newInstance(condition == null
                ? userRoleSerivce.findAllUserRoleMapping(pageable)
                : userRoleSerivce.findUserRoleMappingByCondition(pageable, condition));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:rolepermission')")
    @RequestMapping(value = "all", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
    @ResponseBody
    @JSON(type = Role.class, filter = "permissions")
    public JSONObject getAllData() {
        return newInstance("all", roleService.findAll());
    }

    @RequestMapping(value = "single", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
    @ResponseBody
    public JSONObject getSingleJSONObject(@RequestBody Map<String, Object> map) {
        return newInstance("obj", roleService.findById(Long.valueOf(map.get("id").toString())));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:roleuser')")
    @RequestMapping(value = "singleUserRoleCombineRole", method = RequestMethod.POST, produces = PRODUCES_APPLICATION_JSON)
    @ResponseBody
    public JSONObject getSingleUserRoleCombineRoleJSONData(@RequestBody Map<String, Object> map) {
        return newInstance("obj", userRoleSerivce.findUserRoleMappingByUserId(Long.valueOf(map.get("id").toString())));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:update')")
    @RequestMapping(value = "singleUpdate", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateSingle(@RequestBody Role role) {
        return newInstance("updated", roleService.update(role));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:roleuser')")
    @RequestMapping(value = "/setUserRoles", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addUserRoles(@RequestBody UserRoleCombineRole userRoleCombineRole) {
        return newInstance("updated", userRoleSerivce.updateUserRoleMapping(userRoleCombineRole));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:delete')")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject delete(@RequestBody String ids) {
        return newInstance("count", roleService
                .delete(CommonConvertUtils.convertToLongArray(ids)));
    }

    @RequestMapping(value = "/checkRoleNameUnique", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkRoleNameUnique(@RequestBody Map<String, Object> map) {
        String roleName = map.get("data").toString();
        Long roleId = map.containsKey("id") ? Long.valueOf(map.get("id").toString()) : null;
        return newInstance("isUnique", roleService.checkRoleNameUnique(roleName, roleId));
    }

    @RequestMapping(value = "/isUsedRoles", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject isUsedRoles(@RequestBody String roleIds) {
        return newInstance("isUsed", roleService.isUsedRoles(roleIds));
    }

    @PreAuthorize("authenticated and hasPermission('role','role:rolepermission')")
    @RequestMapping(value = "/allotPermissionsToRole", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject allotPermissionsToRole(@RequestBody RolePermissionMappingDTO rolePermissionMappingDTO) {
        return newInstance("allotted", rolePermissionService.updateRolePermissionMapping(rolePermissionMappingDTO));
    }

    @RequestMapping(value = "/refreshMapping")
    @ResponseBody
    public void refreshMapping() {
        rolePermissionService.refreshRolePermissionMapping();
    }
}
