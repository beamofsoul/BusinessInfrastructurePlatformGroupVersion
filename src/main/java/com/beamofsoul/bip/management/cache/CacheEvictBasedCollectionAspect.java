package com.beamofsoul.bip.management.cache;

import java.util.Collection;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.management.util.CacheUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据缓存对象主键id数组将每一个id对象的业务对象从缓存中清除
 * @author MingshuJian
 */
@Slf4j
@Aspect
@Component
public class CacheEvictBasedCollectionAspect {
	
	@Pointcut(value="@annotation(cacheEvictBasedCollection)")
	public void locateAnnotation(CacheEvictBasedCollection cacheEvictBasedCollection) {}
	
	@SuppressWarnings("unchecked")
	@Before("locateAnnotation(cacheEvictBasedCollection)")
	public void doBefore(JoinPoint joinPoint, CacheEvictBasedCollection cacheEvictBasedCollection) {
		//获取注解参数中的缓存名称、缓存对象的key和目标方法输入参数
		String[] cacheNames = cacheEvictBasedCollection.value();
		String key = cacheEvictBasedCollection.key();
		Object[] params = joinPoint.getArgs();
		Object param = null;
		
		//根据注解参数，如果注解参数中不包含缓存名称，就去目标方法所在的类的CacheConfig注解中上面的参数找缓存名称
		//如果CacheConfig注解不存在或没有配置缓存名称，那么不向下执行其他代码并提示没有设置方法名
		if (cacheNames == null || cacheNames.length == 0 || cacheNames[0].equals("")) {
			CacheConfig config = joinPoint.getTarget().getClass().getDeclaredAnnotation(CacheConfig.class);
			if (config == null || config.cacheNames() == null || config.cacheNames().length == 0) {
				log.warn("no cache names found in annotation CacheEvictBasedCollection");
				return;
			} else {
				cacheNames = config.cacheNames();
			}
		}
		
		//判断注解输入的缓存对象的key是否符合解析规则，并进行解析
		//从而获得需要的实体对象主键id数组对象，否则提示缓存对象的key不能识别
		try {
			if (key.startsWith("#p")) key = key.substring(2);
			param = params[Integer.valueOf(key)];
		} catch (Exception e) {
			log.warn("unrecognized cache key found in annotation CacheEvictBasedCollection",e);
			return;
		}
		
		//通过缓存名称获取缓存，并将解析后的实体对象主键id数组对象遍历
		//以遍历后的每一个id为缓存对象key，将其从缓存中清除
		try {
			Cache cache = null;
			for (String cacheName : cacheNames) {
				cache = CacheUtils.getCache(cacheName);
				if (cache == null) {
					log.warn("no cache named {}", cacheName);
					continue;
				}
				if (param instanceof Long[]) {
					for (Long p : (Long[]) param) cache.evict(p);
				} else if (param instanceof Collection) {
					for (Long p : (List<Long>) param) cache.evict(p);
				} else {
					log.warn("inappropriate input params found in annotation CacheEvictBasedCollection");
					return;
				}
			}
		} catch (Exception e) {
			log.warn("failed to evict cache data using annotation CacheEvictBasedCollection",e);
		}
		
	}
}
