package com.beamofsoul.bip.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.repository.OrganizationRepository;
import com.beamofsoul.bip.service.OrganizationService;
import com.querydsl.core.types.Predicate;

@Service("organizationService")
public class OrganizationServiceImpl extends BaseAbstractServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public Organization create(Organization organization) {
		return organizationRepository.save(organization);
	}

	@Override
	public Organization update(Organization organization) {
		Organization originalOrganization = organizationRepository.findOne(organization.getId());
		BeanUtils.copyProperties(organization, originalOrganization);
		return organizationRepository.save(originalOrganization);
	}

	@Override
	@Transactional
	public long delete(Long... ids) {
		return organizationRepository.deleteByIds(ids);
	}

	@Override
	public Organization findById(Long id) {
		return organizationRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Organization> findAll() {
		return organizationRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Organization> findAll(Pageable pageable) {
		return organizationRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Organization> findAll(Pageable pageable, Predicate predicate) {
		return organizationRepository.findAll(predicate, pageable);
	}
}
