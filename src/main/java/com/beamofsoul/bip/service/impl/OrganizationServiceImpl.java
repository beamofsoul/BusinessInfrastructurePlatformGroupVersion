package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toBooleanValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.entity.Permission;
import com.beamofsoul.bip.entity.query.QOrganization;
import com.beamofsoul.bip.entity.query.QPermission;
import com.beamofsoul.bip.management.cache.CacheableBasedPageableCollection;
import com.beamofsoul.bip.repository.OrganizationRepository;
import com.beamofsoul.bip.service.OrganizationService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("organizationService")
@CacheConfig(cacheNames = "organizationCache")
public class OrganizationServiceImpl extends BaseAbstractServiceImpl implements OrganizationService {

	public static final String CACHE_NAME = "organizationCache";

	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public Organization create(Organization organization) {
		return organizationRepository.save(organization);
	}

	@Transactional
	@CachePut(key="#organization.id")
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
	public List<Organization> findRelationalAll(Predicate predicate) {
		List<Organization> children = organizationRepository.findByPredicateAndSort(predicate, new Sort(Direction.ASC, "sort"));
		loadRelationalInformation(children);
		return children;
	}
	private void loadRelationalInformation(List<Organization> organizations) {
		organizations.stream().forEach(e -> {
			e.setCountOfChildren(organizationRepository.count(QOrganization.organization.parentId.eq(e.getId())));
		});
	}
	@Override
	public BooleanExpression onRelationalSearch(JSONObject content) {
		return new QPermission("Organization").parentId.eq(content.getLongValue("parentId"));
	}

	@Override
	@CacheableBasedPageableCollection(entity = Organization.class)
	@Transactional(readOnly = true)
	public Page<Organization> findAll(Pageable pageable) {
//		return organizationRepository.findAll(pageable);
		return null;
	}

	@Override
	@CacheableBasedPageableCollection(entity = Organization.class)
	@Transactional(readOnly = true)
	public Page<Organization> findAll(Pageable pageable, Predicate predicate) {
//		return organizationRepository.findAll(predicate, pageable);
		return null;
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
//		QPermission permission = new QPermission("Permission");
//		BooleanExpression exp = null;
//		
//		String name = content.getString("name");
//		exp = addExpression(name, exp, permission.name.like(like(name)));
//		
//		String id = content.getString("id");
//		exp = addExpression(id, exp, permission.id.eq(toLongValue(id)));
//		
//		String url = content.getString("url");
//		exp = addExpression(url, exp, permission.url.like(like(url)));
//		
//		String action = content.getString("action");
//		exp = addExpression(action, exp, permission.action.like(like(action)));
//		
//		String group = content.getString("group");
//		exp = addExpression(group, exp, permission.group.like(like(group)));
//		
//		String parentId = content.getString("parentId");
//		exp = addExpression(parentId, exp, permission.parentId.eq(toLongValue(parentId)));
//		
//		String resourceType = content.getString("resourceType");
//		exp = addExpression(resourceType, exp, permission.resourceType.eq(resourceType));
//		
//		String available = content.getString("available");
//		exp = addExpression(available, exp, permission.available.eq(toBooleanValue(available)));
//		
//		return exp;
		return null;
	}
}
