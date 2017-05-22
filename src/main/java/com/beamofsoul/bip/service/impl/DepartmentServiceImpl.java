package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toBooleanValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Department;
import com.beamofsoul.bip.entity.Organization;
import com.beamofsoul.bip.entity.query.QDepartment;
import com.beamofsoul.bip.repository.DepartmentRepository;
import com.beamofsoul.bip.service.DepartmentService;
import com.beamofsoul.bip.service.OrganizationService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("departmentService")
public class DepartmentServiceImpl extends BaseAbstractServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private OrganizationService organizationService;

	@Override
	public Department create(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public Department update(Department department) {
		Department originalDepartment = departmentRepository.findOne(department.getId());
		Department currentParent = department.getParent();
		if (currentParent != null) {
			Department originalParent = originalDepartment.getParent();
			department.setParent(
				(originalParent == null || !originalParent.getId().equals(currentParent.getId())) ? 
					this.findById(currentParent.getId()) : originalParent);
		}
		Organization currentOrganization = department.getOrganization();
		if (currentOrganization != null) {
			Organization originalOrganization = originalDepartment.getOrganization();
			department.setOrganization(
				(originalOrganization == null || !originalOrganization.getId().equals(currentOrganization.getId())) ?
					organizationService.findById(currentOrganization.getId()) : originalOrganization);
		}
		BeanUtils.copyProperties(department, originalDepartment);
		return departmentRepository.save(originalDepartment);
	}

	@Override
	@Transactional
	public long delete(Long... ids) {
		return departmentRepository.deleteByIds(ids);
	}

	@Override
	public Department findById(Long id) {
		return departmentRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Department> findAll() {
		return departmentRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Department> findAll(Pageable pageable) {
		return departmentRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Department> findAll(Pageable pageable, Predicate predicate) {
		return departmentRepository.findAll(predicate, pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Department> findAllAvailableDepartments() {
		return departmentRepository.findByPredicate(new QDepartment("Department").available.eq(true));
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		QDepartment department = QDepartment.department;
		BooleanExpression exp = null;
		
		String code = content.getString("code");
		exp = addExpression(code, exp, department.code.like(like(code)));
		
		String name = content.getString("name");
		exp = addExpression(name, exp, department.name.like(like(name)));

		String id = content.getString("id");
		exp = addExpression(id, exp, department.id.eq(toLongValue(id)));
		
		String parent = content.getString("parent");
		exp = addExpression(parent, exp, department.parent.name.eq(like(parent)));
		
		String available = content.getString("available");
		exp = addExpression(available, exp, department.available.eq(toBooleanValue(available)));
		
		String organization = content.getString("organization");
		exp = addExpression(organization, exp, department.organization.name.like(like(organization)));
		
		return exp;
	}
	
	@Override
	public boolean checkDepartmentCodeUnique(String code, Long id) {
		BooleanExpression predicate = QDepartment.department.code.eq(code);
		if (id != null) predicate = predicate.and(QDepartment.department.id.ne(id));
		return departmentRepository.count(predicate) == 0;
	}

	@Override
	public List<Long> findChildrenIds(Long id) {
		List<BigInteger> result = departmentRepository.findChildrenIds(id);
		return result.stream().map(e -> e.longValue()).collect(Collectors.toList());
	}
}
