package com.beamofsoul.bip.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.ActionMonitor;
import com.beamofsoul.bip.repository.ActionMonitorRepository;
import com.beamofsoul.bip.service.ActionMonitorService;
import com.querydsl.core.types.Predicate;

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
}
