package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.CodeRecord;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

/**
 * @ClassName CodeRecordRepository
 * @Description 系统验证码记录持久化层接口
 * @author MingshuJian
 * @Date 2017年3月24日 上午11:12:56
 * @version 1.0.0
 */
@Repository
public interface CodeRecordRepository extends BaseMultielementRepository<CodeRecord, Long> {

	CodeRecord findByCode(String code);
}
