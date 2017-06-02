package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.SensitiveWord;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface SensitiveWordService {

	SensitiveWord create(SensitiveWord sensitiveWord);
	SensitiveWord update(SensitiveWord sensitiveWord);
	long delete(Long... ids);

	SensitiveWord findById(Long id);

	List<SensitiveWord> findAll();
	Page<SensitiveWord> findAll(Pageable pageable);
	Page<SensitiveWord> findAll(Pageable pageable, Predicate predicate);
	
	BooleanExpression onSearch(JSONObject content);
	boolean findSensitiveFilterOpen();
	void setSensitiveFilterOpen(boolean isOpen);
}
