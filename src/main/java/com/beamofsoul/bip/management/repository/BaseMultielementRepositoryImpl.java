package com.beamofsoul.bip.management.repository;

import static com.beamofsoul.bip.management.util.ConfigurationReader.PROJECT_BASE_REPOSITORY_BATCH_SIZE;
import static com.beamofsoul.bip.management.util.ConfigurationReader.asString;
import static com.beamofsoul.bip.management.util.ConfigurationReader.getValue;
import static com.beamofsoul.bip.management.util.QueryDSLUtils.doDelete;
import static com.beamofsoul.bip.management.util.QueryDSLUtils.doUpdate;
import static com.beamofsoul.bip.management.util.QueryDSLUtils.initQuery;
import static com.beamofsoul.bip.management.util.QueryDSLUtils.newQuery;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.beamofsoul.bip.management.util.Constants;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * @ClassName BaseMultielementRepositoryImpl
 * @Description 默认的基于泛型的持久层通用实现类
 * @author MingshuJian
 * @Date 2017年2月8日 上午11:15:42
 * @version 1.0.0
 * @param <T> - 业务实体类类型
 * @param <ID> - 业务实体类主键类型
 */
@SuppressWarnings("unchecked")
@NoRepositoryBean
public class BaseMultielementRepositoryImpl<T,ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements BaseMultielementRepository<T, ID> {

	private final JpaEntityInformation<T, ID> entityInformation;
    private final EntityManager entityManager;
    private final PathBuilder<T> entityPath;
    private final Path<?> idPath;
    private int batchSize;
    
	public BaseMultielementRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.entityManager = entityManager;
		this.entityPath = getEntityPath();
		this.idPath = getPrimaryKeyPath();
		
		String batchSizeStr = asString(getValue(PROJECT_BASE_REPOSITORY_BATCH_SIZE));
		this.batchSize = StringUtils.isBlank(batchSizeStr) ? 30 : Integer.valueOf(batchSizeStr);
	}
	
	public BaseMultielementRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		this((JpaEntityInformation<T, ID>) JpaEntityInformationSupport
			.getEntityInformation(domainClass, entityManager), entityManager);
	}
	
	/**
	 * @Title: findByIds  
	 * @Description: 根据业务实体类主键数组获取所有相关的业务实体类实例列表
	 * @param ids 业务实体类主键数组
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByIds(ID... ids) {
		return queryByPredicate(((SimpleExpression<ID>) idPath).in(ids));
	}
	
	/**
	 * @Title: findByAttr  
	 * @Description: 根据业务实体类属性名称和属性值获取符合条件的业务实体类实例列表
	 * @param name 业务实体类属性名称
	 * @param value 业务实体类属性名称对应的属性值
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByAttr(String name, Object value) {
		return queryByPredicate(entityPath.get(name).eq(value));
	}
	
	/**
	 * @Title: findByPredicate  
	 * @Description: 根据业务实体类查询条件获取符合条件的业务实体类实例列表
	 * @param predicate 业务实体类的查询条件对象
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByPredicate(Predicate predicate) {
		return queryByPredicate(predicate);
	}
	
	/**
	 * @Title: findByPredicate  
	 * @Description: 根据业务实体类查询条件获取符合条件的业务实体类实例列表
	 * @param predicate 业务实体类的查询条件对象
	 * @param limit 最多返回多少条记录
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByPredicate(Predicate predicate, Long limit) {
		return queryByPredicate(predicate, limit);
	}
	
	/**
	 * @Title: findByPredicate  
	 * @Description: 根据业务实体类查询条件获取符合条件的业务实体类实例列表
	 * @param predicate 业务实体类的查询条件对象
	 * @param limit 最多返回多少条记录
	 * @param sort 排序条件
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByPredicate(Predicate predicate, Long limit, Sort sort) {
		return queryByPredicate(predicate, limit, sort);
	}
	
	/**
	 * @Title: findOneByPredicate  
	 * @Description: 根据业务实体类查询条件获取符合条件的业务实体类实例
	 * @param predicate 业务实体类的查询条件对象
	 * @return T 查询到的业务实体类实例
	 */
	@Override
	public T findOneByPredicate(Predicate predicate) {
		return queryOneByPredicate(predicate);
	}
	
	/**
	 * @Title: findByPredicateAndSort  
	 * @Description: 根据业务实体类查询条件和排序条件获取符合条件的业务实体类实例列表
	 * @param predicate 业务实体类的查询条件对象
	 * @param sort 业务实体类的排序条件对象
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByPredicateAndSort(Predicate predicate, Sort sort) {
		return queryByPredicateAndSort(predicate, sort);
	}
	
	/**
	 * @Title: findByAttrIn  
	 * @Description: 根据业务实体类属性名称和属性值数组获取符合条件的业务实体类实例列表
	 * @param name 业务实体类属性名称
	 * @param value 业务实体类属性名称对应的属性值数组
	 * @return List<T> 查询到的业务实体类实例列表
	 */
	@Override
	public List<T> findByAttrIn(String name, Object... values) {
		return queryByPredicate(entityPath.get(name).in(values));
	}
	
	/**
	 * @Title: findPageableIds  
	 * @Description: 根据分页对象的值查询当前泛型业务实体类某一页所有的主键ID
	 * @param pageable 分页对象，保存了当前页码，每页数量和一个排序对象(Sort)
	 * @return QueryResults<ID> 查询到的业务实体类主键ID列表
	 */
	@Override
	public QueryResults<ID> findPageableIds(Pageable pageable) {
		return findPageableIds(pageable, null);
	}
	
	/**
	 * @Title: findPageableIds  
	 * @Description: 根据分页对象的值查询当前泛型业务实体类某一页所有的主键ID
	 * @param pageable 分页对象，保存了当前页码，每页数量和一个排序对象(Sort)
	 * @param predicate 断言对象，保存了查询的条件
	 * @return QueryResults<ID> 查询到的业务实体类主键ID列表
	 */
	@Override
	public QueryResults<ID> findPageableIds(Pageable pageable, Predicate predicate) {
		JPAQuery<T> query = initQuery(entityManager, entityPath, pageable, predicate);
		query.select(idPath);
		return (QueryResults<ID>) query.fetchResults();
	}

      /**
	 * @Title: findSpecificDataByPredicate
	 * @Description: 根据查询需要的字段查询当前泛型业务实体类的特定结果集
	 * @param predicate 断言对象，保存了查询的条件
       * @param selects 查询需要的字段
	 * @return QueryResults<?> 查询到的业务实体类的特定结果集
	 */
	@Override
	public QueryResults<?> findSpecificDataByPredicate( Predicate predicate, Expression<?>... selects) {
		JPAQuery<T> query = newQuery(entityManager, entityPath, predicate, null, selects);
		return query.fetchResults();
	}

	/**
	 * @Title: deleteByIds  
	 * @Description: 根据业务实体类主键ID删除相对应的业务记录
	 * @param ids 业务实体类主键ID数组
	 * @return long 删除多少条业务记录的数量
	 */
	@Override
	public long deleteByIds(ID... ids) {
		return deleteByPredicate(((SimpleExpression<ID>) idPath).in(ids));
	}
	
	/**
	 * @Title: bulkSave  
	 * @Description: 根据batchSize向数据库中批量插入数据
	 * @param entities 需要插入的数据
	 * @return Collection<T> 已经插入的数据
	 */
	@Override
	public Collection<T> bulkSave(Collection<T> entities) {
		final List<T> savedEntities = new ArrayList<T>(entities.size());
		int i = 0;
		for (T t : entities) {
			entityManager.persist(t);
			savedEntities.add(t);
			i++;
			if (i % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
		return savedEntities;
	}
	
	/**
	 * @Title: update  
	 * @Description: 根据path与value值批量修改数据库表中的记录
	 * @param path 需要修改的字段
	 * @param value 修改后的值
	 * @return long 修改了多少条记录
	 */
	@Override
	public <S> long update(Path<S> path, S value) {
		return doUpdate(entityManager, entityPath, path, value);
	}
	
	/**
	 * @Title: update  
	 * @Description: 根据path与value值批量修改数据库表中的记录
	 * @param path 需要修改的字段
	 * @param value 修改后的值
	 * @param predicate 修改的条件
	 * @return long 修改了多少条记录
	 */
	@Override
	public <S> long update(Path<S> path, S value, Predicate predicate) {
		return doUpdate(entityManager, entityPath, path, value, predicate);
	}
	
	/**
	 * @Title: update  
	 * @Description: 根据path与value值批量修改数据库表中的记录
	 * @param paths 需要修改的字段列表
	 * @param values 修改后的值列表
	 * @return long 修改了多少条记录
	 */
	@Override
	public long update(List<? extends Path<?>> paths, List<?> values) {
		return doUpdate(entityManager, entityPath, paths, values);
	} 
	
	/**
	 * @Title: update  
	 * @Description: 根据path与value值批量修改数据库表中的记录
	 * @param paths 需要修改的字段列表
	 * @param values 修改后的值列表
	 * @param predicate 修改的条件
	 * @return long 修改了多少条记录
	 */
	@Override
	public long update(List<? extends Path<?>> paths, List<?> values, Predicate predicate) {
		return doUpdate(entityManager, entityPath, paths, values, predicate);
	} 
	
	/**
	 * @Title: deleteByPredicate  
	 * @Description: 根据条件删除业务记录
	 * @param predicate 删除的条件
	 * @return Long 删除了多少条记录
	 */
	@Override
	public Long deleteByPredicate(Predicate predicate) {
		return doDelete(entityManager, entityPath, predicate);
	}
	
	/**
	 * @Title: newQueryByPredicate  
	 * @Description: 根据查询条件创建查询对象
	 * @param predicate 查询的条件
	 * @return JPAQuery<T> 创建好的查询对象
	 */
	public JPAQuery<T> newQueryByPredicate(Predicate predicate) {
		return newQuery(entityManager, entityPath, predicate);
	}
	
	/**
	 * @Title: newQueryByPredicateAndSort  
	 * @Description: 根据查询条件和排序条件创建查询对象
	 * @param predicate 查询的条件
	 * @param sort 排序的条件
	 * @return JPAQuery<T> 创建好的查询对象
	 */
	public JPAQuery<T> newQueryByPredicateAndSort(Predicate predicate, Sort sort) {
		return newQuery(entityManager, entityPath, predicate, sort);
	}
	
	/**
	 * @Title: queryByPredicate  
	 * @Description: 根据查询条件执行查询
	 * @param predicate 查询的条件
	 * @return List<T> 执行查询后的返回值
	 */
	public List<T> queryByPredicate(Predicate predicate) {
		return newQueryByPredicate(predicate).fetch();
	}
	
	/**
	 * @Title: queryByPredicate  
	 * @Description: 根据查询条件执行查询
	 * @param predicate 查询的条件
	 * @param limit 最大多少条记录
	 * @return List<T> 执行查询后的返回值
	 */
	public List<T> queryByPredicate(Predicate predicate, Long limit) {
		return newQueryByPredicate(predicate).limit(limit).fetch();
	}
	
	/**
	 * @Title: queryByPredicate  
	 * @Description: 根据查询条件执行查询
	 * @param predicate 查询的条件
	 * @param limit 最大多少条记录
	 * @param sort 排序方法
	 * @return List<T> 执行查询后的返回值
	 */
	public List<T> queryByPredicate(Predicate predicate, Long limit, Sort sort) {
		JPAQuery<T> query = newQuery(entityManager, entityPath, predicate, sort);
		return query.limit(limit).fetch();
	}
	
	/**
	 * @Title: queryOneByPredicate  
	 * @Description: 根据查询条件执行查询
	 * @param predicate 查询的条件
	 * @return T 执行查询后的单一返回值
	 */
	public T queryOneByPredicate(Predicate predicate) {
		return newQueryByPredicate(predicate).fetchOne();
	}
	
	/**
	 * @Title: queryByPredicateAndSort  
	 * @Description: 根据查询条件和排序条件执行查询
	 * @param predicate 查询的条件
	 * @param sort 排序的条件
	 * @return List<T> 执行查询后的返回值
	 */
	public List<T> queryByPredicateAndSort(Predicate predicate, Sort sort) {
		return newQueryByPredicateAndSort(predicate, sort).fetch();
	}
	
	/**
	 * @Title: getEntityPath  
	 * @Description: 根据当前类的泛型业务实体类构建PathBuilder对象实例
	 * @return PathBuilder<T> 实例化完成的PathBuilder对象实例
	 */
	public PathBuilder<T> getEntityPath() {
		return new PathBuilder<T>(entityInformation.getJavaType(), entityInformation.getEntityName());
	}
	
	/**
	 * @Title: getPrimaryKeyPath  
	 * @Description: 根据当前类的泛型业务实体类构建其主键的Path对象实例
	 * @return Path<?> 构建完成的Path对象实例
	 */
	private Path<?> getPrimaryKeyPath() {
		final Type type = ReflectionUtils.getTypeParameter(this.getClass().getGenericSuperclass(), 1);;
		if (type.getTypeName().equals(Long.class.getTypeName())) {
			return entityPath.getNumber(Constants.DEFAULT_ENTITY_PRIMARY_KEY, Long.class);
		} else {
			return entityPath.getString(Constants.DEFAULT_ENTITY_PRIMARY_KEY);
		}
	}
}
