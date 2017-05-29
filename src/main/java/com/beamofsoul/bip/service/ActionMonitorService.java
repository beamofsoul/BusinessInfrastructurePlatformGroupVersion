package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.ActionMonitor;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface ActionMonitorService {

	ActionMonitor create(ActionMonitor actionMonitor);
	ActionMonitor update(ActionMonitor actionMonitor);
	long delete(Long... ids);

	ActionMonitor findById(Long id);
	List<ActionMonitor> findAll();
	Page<ActionMonitor> findAll(Pageable pageable);
	Page<ActionMonitor> findAll(Pageable pageable, Predicate predicate);
	
	BooleanExpression onSearch(JSONObject content);
}
