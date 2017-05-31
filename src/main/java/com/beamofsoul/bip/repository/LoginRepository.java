package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.Login;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

@Repository
public interface LoginRepository extends BaseMultielementRepository<Login, Long> {

}
