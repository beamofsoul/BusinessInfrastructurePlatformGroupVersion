package com.beamofsoul.bip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.Organization;

public interface OrganizationRepositoryCustom {
	
	Page<Organization> findAllChildrenOrganizations(Pageable pageable, Object condition);
	

}
