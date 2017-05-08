package com.beamofsoul.bip.repository;

public interface MessageRepositoryCustom<T, ID> {

	long updateStatusByIds(boolean status, Long... ids);
}
