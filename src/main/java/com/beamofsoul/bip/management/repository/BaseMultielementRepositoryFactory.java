package com.beamofsoul.bip.management.repository;

import javax.persistence.EntityManager;

/**
 * @ClassName BaseMultielementRepositoryFactory
 * @Description 创建BaseMultielementRepository实现类的BaseMultielementRepositoryProvider实例
 * @author MingshuJian
 * @Date 2017年1月24日 上午10:03:12
 * @version 1.0.0
 */
public class BaseMultielementRepositoryFactory implements BaseMultielementRepositoryProvider {
	
	private Class<?> domainClass;
	private EntityManager entityManager;
	
	private BaseMultielementRepositoryFactory() {}
	
	@Override
	public BaseMultielementRepositoryProvider initialize(Object... args) {
		if (args != null && args.length > 0) {
			for (Object object : args) {
				if (object instanceof EntityManager) {
					entityManager = (EntityManager) object;
				} else if (object instanceof Class<?>) {
					domainClass = (Class<?>) object;
				}
			}
		}
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BaseMultielementRepository<?, ?> doProvide() {
		return new BaseMultielementRepositoryImpl(domainClass, entityManager);
	}

	@Override
	public BaseMultielementRepository<?, ?> provide() {
		if (domainClass == null || entityManager == null) {
			throw new RuntimeException("DomainClass or entityManager must not be null");
		}
		return doProvide();
	}
	
	@Override
	public BaseMultielementRepository<?, ?> provide(Object... args) {
		this.initialize(args);
		return doProvide();
	}
	
	@Override
	public Class<?> getReopositoryImplementClass() {
		return BaseMultielementRepositoryImpl.class;
	}
}
