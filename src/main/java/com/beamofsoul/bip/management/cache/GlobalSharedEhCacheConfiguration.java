package com.beamofsoul.bip.management.cache;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;

/**
 * 全局共享缓存配置类
 * <p>使用EhCache实现</p>
 * @author MingshuJian
 */
public abstract class GlobalSharedEhCacheConfiguration {

	@Bean("ehcache")
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}
	
	/**
	 * Spring使用的Cache 
	 */
	@Bean(name = "cacheManager")  
	public EhCacheCacheManager ehCacheCacheManager(){  
	    EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();  
	    ehCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());  
	    return ehCacheCacheManager;  
	}
}
