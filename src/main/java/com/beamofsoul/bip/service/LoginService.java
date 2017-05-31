package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Login;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface LoginService {

	Login create(Login login);
	Login update(Login login);
	long delete(Long... ids);

	Login findById(Long id);

	List<Login> findAll();
	Page<Login> findAll(Pageable pageable);
	Page<Login> findAll(Pageable pageable, Predicate predicate);
	
	BooleanExpression onSearch(JSONObject content);
}
