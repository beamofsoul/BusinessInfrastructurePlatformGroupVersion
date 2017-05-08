package com.beamofsoul.bip.management.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

@SuppressWarnings("unchecked")
@NoRepositoryBean
public interface BaseMultielementRepository<T, ID extends Serializable> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T>,QueryDslPredicateExecutor<T> {

	List<T> findByIds(ID... ids);
	List<T> findByAttr(String name, Object value);
	List<T> findByAttrIn(String name, Object... values);
	List<T> findByPredicate(Predicate predicate);
	List<T> findByPredicate(Predicate predicate, Long limit);
	List<T> findByPredicate(Predicate predicate, Long limit, Sort sort);
	T findOneByPredicate(Predicate predicate);
	List<T> findByPredicateAndSort(Predicate predicate, Sort sort);
	long deleteByIds(ID... ids);
	Collection<T> bulkSave(Collection<T> entities);
	<S> long update(Path<S> path, S value);
	<S> long update(Path<S> path, S value, Predicate predicate);
	long update(List<? extends Path<?>> paths, List<?> values);
	long update(List<? extends Path<?>> paths, List<?> values, Predicate predicate);
	Long deleteByPredicate(Predicate predicate);
	QueryResults<ID> findPageableIds(Pageable pageable);
	QueryResults<ID> findPageableIds(Pageable pageable, Predicate predicate);
    QueryResults<?> findSpecificDataByPredicate(Predicate predicate, Expression<?>... selects);
	PathBuilder<T> getEntityPath();
}
