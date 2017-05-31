package com.beamofsoul.bip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.entity.UserRoleCombineRole;

public interface OrganizationRepositoryCustom {
	
	Page<Organization> findAllChildrenOrganizations(Pageable pageable, Object condition);
//	Organization findOrganizationMaxSort(Long parentId);
	Integer findOrganizationMaxSort(Long parentId);
	
}
