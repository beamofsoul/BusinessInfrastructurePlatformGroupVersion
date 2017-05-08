package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.ActionMonitor;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

@Repository
public interface ActionMonitorRepository extends BaseMultielementRepository<ActionMonitor, Long> {

}
