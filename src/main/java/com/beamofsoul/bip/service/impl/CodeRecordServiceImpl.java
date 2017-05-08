package com.beamofsoul.bip.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.CodeRecord;
import com.beamofsoul.bip.entity.query.QCodeRecord;
import com.beamofsoul.bip.repository.CodeRecordRepository;
import com.beamofsoul.bip.service.CodeRecordService;
import com.querydsl.core.types.Predicate;

import lombok.NonNull;

@Service("codeRecodeService")
public class CodeRecordServiceImpl extends BaseAbstractServiceImpl implements CodeRecordService {

	@Autowired
	private CodeRecordRepository codeRecodeRepository;

	@Override
	@Transactional
	public CodeRecord create(CodeRecord codeRecode) {
		return codeRecodeRepository.save(codeRecode);
	}

	@Override
	@Transactional
	public long delete(@NonNull Long... ids) {
		return codeRecodeRepository.deleteByIds(ids);
	}

	@Override
	public CodeRecord findByCode(String code) {
		return codeRecodeRepository.findByCode(code);
	}

	@Override
	public List<CodeRecord> findAll() {
		return codeRecodeRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<CodeRecord> findAll(Pageable pageable) {
		return codeRecodeRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CodeRecord> findAll(Pageable pageable, Predicate predicate) {
		return codeRecodeRepository.findAll(predicate, pageable);
	}

	@Override
	@Transactional
	public long deleteExpiredOnes(Date date) {
		return codeRecodeRepository.deleteByPredicate(new QCodeRecord("CodeRecord").expiredDate.lt(date == null ? new Date() : date));
	}
}
