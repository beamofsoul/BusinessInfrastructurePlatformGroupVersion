package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beamofsoul.bip.entity.Organization;
import com.querydsl.core.types.Predicate;

public interface OrganizationService {

	Organization create(Organization organization);
	Organization update(Organization organization);
	long delete(Long... ids);

	Organization findById(Long id);

	List<Organization> findAll();
	Page<Organization> findAll(Pageable pageable);
	Page<Organization> findAll(Pageable pageable, Predicate predicate);
}
