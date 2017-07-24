package com.beamofsoul.bip.management.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import lombok.NonNull;

public class CustomRepositoryFactory extends JpaRepositoryFactory {

	@SuppressWarnings("unused")
	private final EntityManager entityManager;
	
	private static BaseMultielementRepositoryProvider baseMultielementRepositoryProvider;
	
	public CustomRepositoryFactory(@NonNull EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
		
		if (baseMultielementRepositoryProvider == null)
			baseMultielementRepositoryProvider = CustomRepositoryConfiguration.getProvider();
	}

	@SuppressWarnings("unchecked")
	protected <T, ID extends Serializable> JpaRepository<T, ID> getTargetRepository(RepositoryMetadata metadata,
			EntityManager entityManager) {
		return (JpaRepository<T, ID>) baseMultielementRepositoryProvider
				.provide((Class<T>) metadata.getDomainType(), entityManager);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T, ID extends Serializable> SimpleJpaRepository<T, ID> getTargetRepository(
			RepositoryInformation information, EntityManager entityManager) {
		return (SimpleJpaRepository<T, ID>) baseMultielementRepositoryProvider
				.provide((Class<T>) information.getDomainType(), entityManager);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return baseMultielementRepositoryProvider.getReopositoryImplementClass();
	}
}
