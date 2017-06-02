package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;
import static com.beamofsoul.bip.management.util.JSONUtils.formatAndParseObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import com.beamofsoul.bip.entity.query.QDepartment;
import com.beamofsoul.bip.entity.query.QOrganization;
import com.beamofsoul.bip.entity.query.QPermission;
import com.beamofsoul.bip.entity.query.QUser;
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
		Integer maxSort = (organization.getParent() == null||organization.getParent().getId()==null) ? null:organizationRepository.findOrganizationMaxSort(organization.getParent().getId());
//		Integer maxSort = organization.getParentId() == null? null:organizationRepository.findOrganizationMaxSort(organization.getParentId());
		if (maxSort != null) organization.setSort(maxSort+1);
		else organization.setSort(1);
		
//		Organization o = organizationRepository.findOrganizationMaxSort(organization.getParentId());
//		if (o ==null || o.getSort() == null)  organization.setSort(1); 
//		else organization.setSort(o.getSort()+1);
//		
////		System.out.println(o.getSort());
//		System.out.println(organization.getSort());
		
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
	
//	@Transactional
//	@CachePut(key="#organization.id")
//	@Override
//	public Organization moveUp(Organization organization) {
//		Organization originalOrganization = organizationRepository.findOne(organization.getId());
//		BeanUtils.copyProperties(organization, originalOrganization);
//		return organizationRepository.save(originalOrganization);
//	}
	
	@Transactional
//	@CachePut(key="#organization.id")
	@Override
	public List<Organization> changeSort(Long beforeId,Long afterId) {
		Organization beforeOrganization = organizationRepository.findOne(beforeId);
		Organization afterOrganization = organizationRepository.findOne(afterId);
//		BeanUtils.copyProperties(organization, originalOrganization);
		Integer beforeSort = beforeOrganization.getSort();
		Integer afterSort = afterOrganization.getSort();
		beforeOrganization.setSort(afterSort);
		afterOrganization.setSort(beforeSort);
		List<Organization> changeSortOrganization = new ArrayList<Organization>();
		changeSortOrganization.add(beforeOrganization);
		changeSortOrganization.add(afterOrganization);
		return organizationRepository.save(changeSortOrganization);
	}
	
	@Override
	@Transactional
	public long deleteNodes(List<Long> parentIds,List<Long> childrenIds){
		List<BigInteger> result = null;
		List<Long> resultDeleteIds = new ArrayList<Long>();
		for (Long parentId : parentIds) {
			if(resultDeleteIds.contains(parentId)){
				continue;
			}
			resultDeleteIds.add(parentId);
			result = organizationRepository.findChildrenIds(parentId);
			resultDeleteIds.addAll(result.stream().map(e -> e.longValue()).collect(Collectors.toList()));
		}
		
		for (Long childrenId : childrenIds) {
			if(resultDeleteIds.contains(childrenId)){
				continue;
			}else{
				resultDeleteIds.add(childrenId);
			}
		}
		Long[] deleteIdsResult = (Long[]) resultDeleteIds.toArray(new Long[0]);
		return organizationRepository.deleteByIds(deleteIdsResult);
		
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
//			e.setCountOfChildren(organizationRepository.count(QOrganization.organization.parentId.eq(e.getId())));
			e.setCountOfChildren(organizationRepository.count(QOrganization.organization.parent.id.eq(e.getId())));
		});
	}
	@Override
	public BooleanExpression onRelationalSearch(JSONObject content) {
		
		QOrganization organization = new QOrganization("Organization");
		Long parentId = content.getLongValue("parentId");
		if (parentId != 0L)
			return organization.parent.id.eq(parentId);
		return organization.parent.id.isNull();
		
//		return new QPermission("Organization").parentId.eq(content.getLongValue("parentId"));
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
	@Transactional(readOnly = true)
	public Page<Organization> findAllChildrenOrganizations(Pageable pageable, Object condition) {
		//根据条件 查询节点下级所有数据
		if(condition!=null){
			Object selectedNodeId = formatAndParseObject(condition.toString()).get("selectedNodeId");
			List<Long> idsLong = null;
			if(selectedNodeId!=null&&StringUtils.isNotBlank(selectedNodeId.toString())){
				List<BigInteger> result = organizationRepository.findChildrenIds(Long.valueOf(selectedNodeId.toString()));
				idsLong =  result.stream().map(e -> e.longValue()).collect(Collectors.toList());
				if(idsLong!=null){
					idsLong.add(0, Long.valueOf(selectedNodeId.toString()));
				}else{
					idsLong = new ArrayList<Long>();
					idsLong.add(Long.valueOf(selectedNodeId.toString()));
				}
			}
			
			return organizationRepository.findAll(this.onSearch(formatAndParseObject(condition.toString()),idsLong), pageable);
			
		}else{
			return this.findAll(pageable);
		}
		
	}
	
	
	@Override
	public BooleanExpression onSearch(JSONObject content,List<Long> idsLong) {
		
//		QPermission permission = new QPermission("Permission");
		QOrganization organization = QOrganization.organization;
		BooleanExpression exp = null;
		
//		String selectedNodeId = content.getString("selectedNodeId");
//		exp = addExpression(selectedNodeId, exp, permission.name.like(like(name)));
		
		if(idsLong!=null&&idsLong.size()!=0){
			exp = addExpression(idsLong.toString(), exp, organization.id.in(idsLong));
		}
		
//		
		String id = content.getString("id");
		exp = addExpression(id, exp, organization.id.eq(toLongValue(id)));
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
		return exp;
//		return null;
	}
	
	@Override
	public List<Organization> findAllAvailableOrganizations() {
		return organizationRepository.findByPredicate(new QOrganization("Organization").available.eq(true));
	}
	
	@Override
	public boolean checkNameUnique(String name, Long id) {
		BooleanExpression predicate = QOrganization.organization.name.eq(name);
		if (id != null) {
			predicate = predicate.and(QOrganization.organization.id.ne(id));
		}
		return organizationRepository.count(predicate) == 0;
	}
	
}
