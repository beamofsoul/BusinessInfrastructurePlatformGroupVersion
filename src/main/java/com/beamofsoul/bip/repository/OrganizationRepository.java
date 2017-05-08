package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

@Repository
public interface OrganizationRepository extends BaseMultielementRepository<Organization, Long> {

}
