package com.beamofsoul.bip.management.repository;

import static com.beamofsoul.bip.management.util.ConfigurationReader.*;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName BaseMultielementRepositoryFactoryConfiguration
 * @Description 设置BaseMultielementRepositoryFactory的一些参数，如使用哪个实现类
 * @author MingshuJian
 * @Date 2017年1月24日 上午11:14:37
 * @version 1.0.0
 */
@Configuration
public class CustomRepositoryConfiguration {

	/**
	 * BaseMultielementRepositoryFactory的默认全路径
	 * 注意：此处需要重构，应将该值持久化到特定数据库表中，以便动态更改
	 */
	private static final String DEFAULT_FACTORY_PATH 
		= "com.beamofsoul.bip.management.repository.BaseMultielementRepositoryFactory";
	
	/**
	 * application.yml配置文件中配置的BaseMultielementRepositoryFactory的全路径
	 */
	//@Value("${project.base.repository.factory}") 
	//@Value不能标识在static field上
    private static String factoryPath;
	
	/**
	 * 暴露出来的factory对象实例
	 * 当有其他类需要在该配置类注册(向Spring容器)factory之前获取factory实例
	 * 该实例句柄将会指向Spring容器中factory对象实例
	 */
	public static BaseMultielementRepositoryFactory factoryInstance;
	
	/**
	 * @Title: baseMultielementRepositoryFactory  
	 * @Description: 向Spring容器中注入Bean: BaseMultielementRepositoryFactory  
	 * @return BaseMultielementRepositoryFactory 返回类型  
	 */
	@Bean
	@Primary
	public BaseMultielementRepositoryFactory baseMultielementRepositoryFactory() {
		return factoryInstance == null ? getFactory() : factoryInstance;
	}
	
	/**
	 * @Title: getFactory  
	 * @Description: 根据配置文件中的参数或默认的factory全路径实例化特定factory  
	 * @return BaseMultielementRepositoryFactory 返回类型  
	 */
	@SuppressWarnings("static-access")
	public static BaseMultielementRepositoryFactory getFactory() {
		/**
		 * 获取BaseMultielementRepositoryFactory实现类全路径
		 */
		factoryPath = asString(getValue(PROJECT_BASE_REPOSITORY_FACTORY));
		/**
		 * 为了保持单例，如果已经存在factory，则不再进行重新创建实例
		 */
		if (factoryInstance != null) {
			return factoryInstance;
		}
		/**
		 * 实例化factory，如未配置则取默认全路径进行实例化
		 */
		try {
			Class<?> clazz = CustomRepositoryConfiguration.class
					.forName(StringUtils.isBlank(factoryPath) ? DEFAULT_FACTORY_PATH : factoryPath);
			Constructor<?> noArgsConstructor = clazz.getDeclaredConstructor(new Class[]{});
			noArgsConstructor.setAccessible(true);
			factoryInstance = (BaseMultielementRepositoryFactory) noArgsConstructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unrecognized factory settings", e);
		}
		return factoryInstance;
	}
}
