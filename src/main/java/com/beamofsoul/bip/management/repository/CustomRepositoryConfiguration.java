package com.beamofsoul.bip.management.repository;

import static com.beamofsoul.bip.management.util.ConfigurationReader.*;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName CustomRepositoryConfiguration
 * @Description 设置BaseMultielementRepositoryProvider的一些参数，如使用哪个实现类
 * @author MingshuJian
 * @Date 2017年1月24日 上午11:14:37
 * @version 1.0.0
 */
@Configuration
public class CustomRepositoryConfiguration {

	/**
	 * BaseMultielementRepositoryProvider实现类的默认全路径
	 */
	private static final String DEFAULT_PROVIDER_INSTANCE_PATH 
		= "com.beamofsoul.bip.management.repository.DefaultBaseMultielementRepositoryProvider";
	
	/**
	 * application.yml配置文件中配置的BaseMultielementRepositoryProvider的全路径
	 */
    private static String providerInstancePath;
	
	/**
	 * 暴露出来的provider对象实例
	 * 当有其他类需要在该配置类注册(向Spring容器)provider之前获取provider实例
	 * 该实例句柄将会指向Spring容器中provider对象实例
	 */
	public static BaseMultielementRepositoryProvider provider;
	
	/**
	 * @Title: baseMultielementRepositoryProvider  
	 * @Description: 向Spring容器中注入Bean: BaseMultielementRepositoryProvider  
	 * @return BaseMultielementRepositoryProvider 返回类型  
	 */
	@Bean
	@Primary
	public BaseMultielementRepositoryProvider baseMultielementRepositoryProvider() {
		return provider == null ? getProvider() : provider;
	}
	
	/**
	 * @Title: getProvider  
	 * @Description: 根据配置文件中的参数或默认的provider instance全路径实例化特定provider实例  
	 * @return BaseMultielementRepositoryProvider 返回类型  
	 */
	@SuppressWarnings("static-access")
	public static BaseMultielementRepositoryProvider getProvider() {
		/**
		 * 为了保持单例，如果已经存在provider实例，则不再进行重新创建实例
		 */
		if (provider != null) return provider;
		/**
		 * 获取BaseMultielementRepositoryProvider实现类全路径
		 */
		providerInstancePath = asString(getValue(PROJECT_BASE_REPOSITORY_PROVIDER));
		/**
		 * 实例化provider，如未配置则取默认全路径进行实例化
		 */
		try {
			Class<?> clazz = CustomRepositoryConfiguration.class
					.forName(StringUtils.isBlank(providerInstancePath) ? DEFAULT_PROVIDER_INSTANCE_PATH : providerInstancePath);
			Constructor<?> noArgsConstructor = clazz.getDeclaredConstructor(new Class[]{});
			noArgsConstructor.setAccessible(true);
			provider = (BaseMultielementRepositoryProvider) noArgsConstructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unrecognized base multielement repository provider settings", e);
		}
		return provider;
	}
}
