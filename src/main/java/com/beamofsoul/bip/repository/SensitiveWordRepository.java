package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.SensitiveWord;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

@Repository
public interface SensitiveWordRepository extends BaseMultielementRepository<SensitiveWord, Long> {

}
