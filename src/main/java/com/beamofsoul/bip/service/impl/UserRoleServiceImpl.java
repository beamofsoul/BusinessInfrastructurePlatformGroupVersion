package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.JSONUtils.formatAndParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.UserRole;
import com.beamofsoul.bip.entity.UserRoleCombineRole;
import com.beamofsoul.bip.entity.query.QRole;
import com.beamofsoul.bip.entity.query.QUser;
import com.beamofsoul.bip.management.util.CacheUtils;
import com.beamofsoul.bip.management.util.CollectionUtils;
import com.beamofsoul.bip.management.util.PageUtils;
import com.beamofsoul.bip.repository.UserRoleRepository;
import com.beamofsoul.bip.service.RoleService;
import com.beamofsoul.bip.service.UserRoleService;
import com.beamofsoul.bip.service.UserService;

@Service("userRoleService")
public class UserRoleServiceImpl extends BaseAbstractServiceImpl implements UserRoleService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Override
	public Page<UserRoleCombineRole> findAllUserRoleMapping(Pageable pageable) {
		pageable = PageUtils.setPageableSort(pageable, new Sort(Direction.ASC, "userId"));
		return userRoleRepository.findAllUserRoleMappingViaView(pageable);
	}
	
	@Override
	public Page<UserRoleCombineRole> findUserRoleMappingByCondition(Pageable pageable, Object condition) {
		return userRoleRepository.findUserRoleMappingByConditionViaView(pageable, 
				formatAndParseObject(condition.toString()).get("ids"));
	}

	@Override
	public UserRoleCombineRole findUserRoleMappingByUserId(Long userId) {
		return userRoleRepository.findUserRoleMappingByUserId(userId);
	}

	@Transactional
	@Override
	public UserRoleCombineRole updateUserRoleMapping(UserRoleCombineRole userRoleCombineRole) {
		// 1. 获取未修改的原始数据
		UserRoleCombineRole originalObject = this.findUserRoleMappingByUserId(userRoleCombineRole.getUserId());
		// 2. 解析原始数据与方法输入的当前修改数据
		List<String> originalRoleIdsList = Arrays.asList(originalObject.getRoleId().split(","));
		List<String> currentRoleIdsList = Arrays.asList(userRoleCombineRole.getRoleId().split(","));
		// 3. 定义需要增加的对象集合和需要删除的对象主键Id集合
		List<UserRole> neededObjList = new ArrayList<>(currentRoleIdsList.size());
		List<Long> abandonedList = new ArrayList<>(originalRoleIdsList.size());
		Long userId = userRoleCombineRole.getUserId();
		// 4. 初始化两个集合中的数据，将当前有而原始数据中没有的角色增加到需要增加的对象集合，将当前没有而原始数据中存在的角色增加到需要删除的对象主键Id集合中
		if(CollectionUtils.isNotBlank(currentRoleIdsList)) currentRoleIdsList.stream().filter(e->!originalRoleIdsList.contains(e)).forEach(e-> neededObjList.add(new UserRole(userService.findById(userId),roleService.findById(Long.valueOf(e)))));
		if(CollectionUtils.isNotBlank(originalRoleIdsList)) originalRoleIdsList.stream().filter(e->!currentRoleIdsList.contains(e)).forEach(e-> abandonedList.add(Long.valueOf(e)));
		// 5. 执行批量增加和删除
		if (neededObjList.size() > 0) this.bulkCreate(neededObjList);
		if (abandonedList.size() > 0) this.deleteByUserIdAndRoleIds(userId, abandonedList.toArray(new Long[abandonedList.size()]));
		// 6. 清除缓存中符合增加或删除条件的记录
		CacheUtils.remove("userCache", userId);
		// 7. 根据用户主键Id重新查询用户角色视图记录，并返回以便展示新的数据
		return userRoleRepository.findUserRoleMappingByUserId(userId);
	}

	@Override
	public UserRole create(UserRole userRole) {
		return userRoleRepository.save(userRole);
	}
	
	@Override
	public Collection<UserRole> bulkCreate(Collection<UserRole> userRoles) {
		return userRoleRepository.bulkSave(userRoles);
	}

	@Override
	public void delete(Long id) {
		userRoleRepository.delete(id);
	}

	@Override
	public Long deleteByIds(Long... ids) {
		return userRoleRepository.deleteByIds(ids);
	}

	@Override
	public Long deleteByUserIdAndRoleIds(Long userId, Long[] roleIds) {
		//此处如果使用QUserRole.user.id和QUserRole.role.id会出现异常
		//org.hibernate.hql.internal.ast.InvalidPathException: Invalid path: 'userRole.role.id'
		//跟踪进hibernate源代码后发现getImports映射不到实体类，所以在HQL转SQL时不能找到正确的对象路径
		//猜测为QueryDSL与Hibernate兼容问题或QueryDSL查询文件生成的问题
		//已经将问题在GitHub上进行了反应, 该问题的URL: https://github.com/querydsl/querydsl/issues/2102
		return userRoleRepository.deleteByPredicate(QRole.role.id.in(roleIds).and(QUser.user.id.eq(userId)));
	}
}
