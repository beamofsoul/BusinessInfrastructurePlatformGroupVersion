package com.beamofsoul.bip.management.repository;

/**
 * @ClassName BaseMultielementRepositoryProvider
 * @Description BaseMultielementRepository实例提供器
 * @author MingshuJian
 * @Date 2017年7月24日 上午10:23:32
 * @version 1.0.0
 */
public interface BaseMultielementRepositoryProvider {
	
	BaseMultielementRepositoryProvider initialize(Object... args);
	BaseMultielementRepository<?, ?> provide();
	BaseMultielementRepository<?, ?> provide(Object... args);
	Class<?> getReopositoryImplementClass();
}
