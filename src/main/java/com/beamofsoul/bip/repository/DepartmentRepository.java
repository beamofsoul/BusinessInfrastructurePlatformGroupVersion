package com.beamofsoul.bip.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.Department;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

@Repository
public interface DepartmentRepository extends BaseMultielementRepository<Department, Long> {

	@Query(nativeQuery = true, value="select id from (select * from t_department order by parent_id, id) department, (select @pv \\:= ?1) initialisation where find_in_set(parent_id, @pv) > 0 and @pv \\:= concat(@pv, ',', id)")
	public List<BigInteger> findChildrenIds(Long id);
}
