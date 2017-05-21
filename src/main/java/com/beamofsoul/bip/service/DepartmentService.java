package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Department;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface DepartmentService {

	Department create(Department department);
	Department update(Department department);
	long delete(Long... ids);

	Department findById(Long id);

	List<Department> findAll();
	Page<Department> findAll(Pageable pageable);
	Page<Department> findAll(Pageable pageable, Predicate predicate);
	List<Department> findAllAvailableDepartments();
	List<Long> findChildrenIds(Long id);
	
	BooleanExpression onSearch(JSONObject content);
	boolean checkDepartmentCodeUnique(String code, Long id);
}
