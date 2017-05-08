package com.beamofsoul.bip.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.CodeRecord;
import com.querydsl.core.types.Predicate;

public interface CodeRecordService {

	CodeRecord create(CodeRecord codeRecord);
	long delete(Long... ids);
	long deleteExpiredOnes(Date date);
	List<CodeRecord> findAll();
	Page<CodeRecord> findAll(Pageable pageable);
	Page<CodeRecord> findAll(Pageable pageable, Predicate predicate);
	CodeRecord findByCode(String code);
}
