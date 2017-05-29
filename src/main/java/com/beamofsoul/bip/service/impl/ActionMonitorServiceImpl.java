package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toIntegerValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.ActionMonitor;
import com.beamofsoul.bip.entity.query.QActionMonitor;
import com.beamofsoul.bip.repository.ActionMonitorRepository;
import com.beamofsoul.bip.service.ActionMonitorService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("actionMonitorService")
public class ActionMonitorServiceImpl extends BaseAbstractServiceImpl implements ActionMonitorService {

	@Autowired
	private ActionMonitorRepository actionMonitorRepository;

	@Override
	public ActionMonitor create(ActionMonitor actionMonitor) {
		return actionMonitorRepository.save(actionMonitor);
	}

	@Override
	public ActionMonitor update(ActionMonitor actionMonitor) {
		ActionMonitor originalActionMonitor = actionMonitorRepository.findOne(actionMonitor.getId());
		BeanUtils.copyProperties(actionMonitor, originalActionMonitor);
		return actionMonitorRepository.save(originalActionMonitor);
	}

	@Override
	@Transactional
	public long delete(Long... ids) {
		return actionMonitorRepository.deleteByIds(ids);
	}

	@Override
	public ActionMonitor findById(Long id) {
		return actionMonitorRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionMonitor> findAll() {
		return actionMonitorRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ActionMonitor> findAll(Pageable pageable) {
		return actionMonitorRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ActionMonitor> findAll(Pageable pageable, Predicate predicate) {
		return actionMonitorRepository.findAll(predicate, pageable);
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		QActionMonitor actionMonitor = QActionMonitor.actionMonitor;
		BooleanExpression exp = null;
		
		String id = content.getString("id");
		exp = addExpression(id, exp, actionMonitor.id.eq(toLongValue(id)));
		
		String user = content.getString("user");
		exp = addExpression(user, exp, actionMonitor.user.nickname.like(like(user)));
		
		String specificAction = content.getString("specificAction");
		exp = addExpression(specificAction, exp, actionMonitor.specificAction.like(like(specificAction)));
		
		String target = content.getString("target");
		exp = addExpression(target, exp, actionMonitor.target.like(like(target)));
		
		String effect = content.getString("effect");
		exp = addExpression(effect, exp, actionMonitor.effect.like(like(effect)));
		
		String hazardLevel = content.getString("hazardLevel");
		exp = addExpression(hazardLevel, exp, actionMonitor.hazardLevel.eq(toIntegerValue(hazardLevel)));
		
		String ipAddress = content.getString("ipAddress");
		exp = addExpression(ipAddress, exp, actionMonitor.ipAddress.like(like(ipAddress)));
		
		String macAddress = content.getString("macAddress");
		exp = addExpression(macAddress, exp, actionMonitor.macAddress.like(like(macAddress)));
		
		return exp;
	}
}
