package com.beamofsoul.bip.management.cache;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.management.repository.BaseMultielementRepository;
import com.beamofsoul.bip.management.repository.BaseMultielementRepositoryFactory;
import com.beamofsoul.bip.management.util.CacheUtils;
import com.beamofsoul.bip.management.util.CollectionUtils;
import com.beamofsoul.bip.management.util.Constants;
import com.beamofsoul.bip.management.util.PageImpl;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 根据分页对象从缓存和数据库中获取需要的业务对象列表
 * 并同步缓存与数据库中这些数据的内容
 * @author MingshuJian
 * @param <K>
 */
@Slf4j
@Aspect
@Component
public class CacheableBasedPageableCollectionAspect<K> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Cache cache;
	
	@Autowired
	private BaseMultielementRepositoryFactory baseMultielementRepositoryFactory;
	
	@Pointcut(value="@annotation(cacheableBasedPageableCollection)")
	public void locateAnnotation(CacheableBasedPageableCollection cacheableBasedPageableCollection) {}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Around("locateAnnotation(cacheableBasedPageableCollection)")
	public Object doAround(ProceedingJoinPoint joinPoint, CacheableBasedPageableCollection cacheableBasedPageableCollection) {
		//获取注解参数中的缓存名称、业务实体类类型和目标方法输入参数(第一个参数必须是分页对象Pageable)
		String[] cacheNames = cacheableBasedPageableCollection.value();
		Class<? extends Serializable> entityClass = cacheableBasedPageableCollection.entity();
		Object[] params = joinPoint.getArgs();
		Object pageableParam = null;
		Object predicateParam = null;
		
		//根据注解参数，如果注解参数中不包含缓存名称，就去目标方法所在的类的CacheConfig注解中上面的参数找缓存名称
		//如果CacheConfig注解不存在或没有配置缓存名称，那么不向下执行其他代码并提示没有设置方法名
		if (cacheNames == null || cacheNames.length == 0 || cacheNames[0].equals("")) {
			CacheConfig config = joinPoint.getTarget().getClass().getDeclaredAnnotation(CacheConfig.class);
			if (config == null || config.cacheNames() == null || config.cacheNames().length == 0) {
				log.warn("no cache names found in annotation CacheableBasedPageableCollection");
				return null;
			} else {
				cacheNames = config.cacheNames();
			}
		}
		
		//判断目标方法存在输入参数且第一个输入参数是分页对象Pageable
		//否则不向下执行其他代码并提示目标方法没有输入参数或输入参数不能识别
		if (params != null && params.length > 0) {
			pageableParam = params[0];
			if (!(pageableParam instanceof Pageable)) {
				log.warn("unrecognized input params found in annotation CacheableBasedPageableCollection");
				return null;
			}
			if (params.length > 1 && params[1] instanceof Predicate) {
				predicateParam = params[1];
			}
		} else {
			log.warn("none input params in function which used annotation CacheableBasedPageableCollection");
			return null;
		}
		
		try {
			//根据具体业务实体类类型和分页对象获取分页后的业务实体类对象主键id列表
			BaseMultielementRepository<?, Long> reps =
					(BaseMultielementRepository<?, Long>) baseMultielementRepositoryFactory
					.init(entityClass, entityManager).doInstance();
			QueryResults<Long> queryResults = 
					reps.findPageableIds((Pageable) pageableParam, 
					predicateParam == null ? null : (Predicate) predicateParam);
			List<Long> ids = queryResults.getResults();
			List<Long> sortedIds = ids.stream().map(e -> e.longValue()).collect(Collectors.toList());
			
			//根据主键id列表去相应的缓存中查询是否有相应的缓存记录
			//将id在缓存中有的对象存入返回结果集，将id在缓存中不存在的对象id存入一个列表中
			List<Long> cachedIds = null;
			for (String cacheName : cacheNames) {
				cache = CacheUtils.getCache(cacheName);
				if (cache != null) {
					cachedIds = ids.stream().filter(e -> (cache.get(e, entityClass) != null)).collect(Collectors.toList());
				}
				if (CollectionUtils.isNotBlank(ids)) {
					ids.removeAll(cachedIds);
				}
				cachedIds.clear();
			}
			
			//当未缓存对象id列表中至少有一个id记录时，去数据库查询该id对应的记录
			//将结果通过其主键id存入缓存中，且将结果存入返回结果集中
			if (ids.size() > 0) {
				List uncachedEntities = reps.findByIds(ids.toArray(new Long[]{}));
				for (String cacheName : cacheNames) {
					cache = CacheUtils.getCache(cacheName);
					if (cache != null) {
						for (Object object : uncachedEntities) {
							cache.put(getEntityPrimaryKeyValue(object), object);
						}
						break;
					}
				}
			}
			
			//2017-03-10 解决缓存数据与从数据库查出来数据合并时的乱序问题，现将所有为缓存记录存入缓存，再遵循顺序一次查出
			List entityList = sortedIds.stream().map(e -> cache.get(e, entityClass)).collect(Collectors.toList());
			
			//将返回结果集中的对象封装成页面展示需要的分页对象并进行返回
			return new PageImpl<>(entityList,(Pageable)pageableParam,queryResults.getTotal());
		} catch (Exception e) {
			log.warn("failed to cache data using annotation CacheableBasedPageableCollection",e);
			return null;
		}
	}
	
	private Long getEntityPrimaryKeyValue(@NonNull Object obj) throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Field pk = obj.getClass().getDeclaredField(Constants.DEFAULT_ENTITY_PRIMARY_KEY);
		pk.setAccessible(true);
		return Long.valueOf(String.valueOf(pk.get(obj)));
	}
}
