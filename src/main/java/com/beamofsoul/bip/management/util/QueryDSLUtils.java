package com.beamofsoul.bip.management.util;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.NonNull;

/**
 * @ClassName QueryDSLUtils
 * @Description 封装了QueryDSL系列对象的工具类
 * @author MingshuJian
 * @Date 2017年2月13日 上午10:45:05
 * @version 1.0.0
 */
public class QueryDSLUtils {

	public static <T> JPAQuery<T> newQuery(@NonNull EntityManager entityManager) {
		return new JPAQuery<T>(entityManager);
	}
	
	public static <T> JPAQuery<T> newQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath) {
		return newQuery(entityManager, entityPath, null);
	}
	
	public static <T> JPAQuery<T> newQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate) {
		return newQuery(entityManager, entityPath, predicate, new Expression<?>[]{});
	}
	
	public static <T> JPAQuery<T> newQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate, Expression<?>... selects) {
		return newQuery(entityManager, entityPath, predicate, null, selects);
	}
	
	public static <T> JPAQuery<T> newQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate, Sort sort, Expression<?>... selects) {
		JPAQuery<T> query = new JPAQuery<T>(entityManager).from(entityPath);
		if (selects != null && selects.length > 0) query.select(selects); 
		if (predicate != null) query.where(predicate);
		if (sort != null) loadSort(query, entityPath, sort);
		return query;
	}
	
	public static JPADeleteClause newDelete(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath) {
		return new JPADeleteClause(entityManager, entityPath);
	}
	
	public static JPADeleteClause newDelete(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, entityPath);
		if (predicate != null) delete.where(predicate);
		return delete;
	}
	
	public static JPAUpdateClause newUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath) {
		return new JPAUpdateClause(entityManager, entityPath);
	}
	
	public static <S> JPAUpdateClause newUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull Path<S> path, S value) {
		JPAUpdateClause update = newUpdate(entityManager, entityPath);
		if (value == null) {
			update.setNull(path);
		} else {
			update.set(path, value);
		}
		return update;
	}
	
	public static <S> JPAUpdateClause newUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull Path<S> path, S value, Predicate predicate) {
		JPAUpdateClause update = newUpdate(entityManager, entityPath, path, value);
		if (predicate != null) update.where(predicate);
		return update;
	}
	
	public static <S> JPAUpdateClause newUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull List<? extends Path<?>> paths,@NonNull List<?> values) {
		return newUpdate(entityManager, entityPath).set(paths, values);
	}
	
	public static <S> JPAUpdateClause newUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull List<? extends Path<?>> paths,@NonNull List<?> values, Predicate predicate) {
		JPAUpdateClause update = newUpdate(entityManager, entityPath, paths, values);
		if (predicate != null) update.where(predicate);
		return update;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> JPAQuery<T> initQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath,@NonNull Pageable pageable, Predicate predicate) {
		/**
		 * 创建查询对象，并将基础属性赋值，如查询的内容与从哪张表中查询
		 */
		JPAQuery<T> query = newQuery(entityManager,entityPath);
		/**
		 * 如果输入参数predicate存在，则将查询的条件设置到查询对象中
		 */
		if (predicate != null) query.where(predicate);
		/**
		 * 将分页属性(包括：当前页码和每页读取多少条记录)设置到查询对象中
		 */
		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		PathBuilder<?> pathBuilder = new PathBuilder(entityPath.getType(), entityPath.getMetadata());
		for (Sort.Order o : pageable.getSort()) {
			query.orderBy(new OrderSpecifier(o.isAscending() ? 
				Order.ASC : Order.DESC, 
					pathBuilder.get(o.getProperty())));
		}
		return query;
	}
	
	public static <T> Page<T> doQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath,@NonNull Pageable pageable) {
		return doQuery(entityManager,entityPath,pageable,null);
	}
	
	public static <T> Page<T> doQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath,@NonNull Pageable pageable, Predicate predicate) {
		JPAQuery<T> query = initQuery(entityManager, entityPath, pageable, predicate);
		QueryResults<T> qr = query.fetchResults();
		return new PageImpl<>(qr.getResults(),pageable,qr.getTotal());
	}
	
	public static <T> T doQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate) {
		JPAQuery<T> query = newQuery(entityManager, entityPath, predicate);
		return query.fetchOne();
	}
	
	public static <T> List<T> doQuery(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, Predicate predicate, Expression<?>... selects) {
		JPAQuery<T> query = newQuery(entityManager, entityPath, predicate, selects);
		return query.fetch();
	}
	
	public static long doDelete(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath) {
		return newDelete(entityManager, entityPath).execute();
	}
	
	public static long doDelete(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull Predicate predicate) {
		return newDelete(entityManager, entityPath, predicate).execute();
	}
	
	public static <S> long doUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull Path<S> path, S value) {
		return  newUpdate(entityManager, entityPath, path, value).execute();
	}
	
	public static <S> long doUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull Path<S> path, S value, Predicate predicate) {
		return newUpdate(entityManager, entityPath, path, value, predicate).execute();
	}
	
	public static <S> long doUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull List<? extends Path<?>> paths,@NonNull List<?> values) {
		return newUpdate(entityManager, entityPath, paths, values).execute();
	}
	
	public static <S> long doUpdate(@NonNull EntityManager entityManager, @NonNull EntityPathBase<?> entityPath, @NonNull List<? extends Path<?>> paths,@NonNull List<?> values, Predicate predicate) {
		return newUpdate(entityManager, entityPath, paths, values, predicate).execute();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadSort(JPAQuery<?> query, EntityPathBase<?> entityPath, Sort sort) {
		PathBuilder<?> pathBuilder = new PathBuilder(entityPath.getType(), entityPath.getMetadata());
		for (Sort.Order o : sort) {
			query.orderBy(new OrderSpecifier(o.isAscending() ? 
				Order.ASC : Order.DESC, 
					pathBuilder.get(o.getProperty())));
		}
	}
}
