package com.beamofsoul.bip.management.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableJpaRepositories(
		basePackages={"com.beamofsoul.bip.repository"},
		repositoryFactoryBeanClass=CustomRepositoryFactoryBean.class)
@EnableSpringDataWebSupport
public class JpaRepositoryConfiguration {

}
