package com.beamofsoul.bip.management.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.hibernate.engine.spi.SessionImplementor;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DatabaseMetadataUtils
 * @Description 项目所使用数据库相关的工具类
 * @author MingshuJian
 * @Date 2017年4月7日 上午8:52:09
 * @version 1.0.0
 */
@Slf4j
public class DatabaseUtils {

	public static EntityManager entityManager = SpringUtils.getBean(EntityManager.class);
	public static Set<String> tableNames = new TreeSet<>();
	
	public static DatabaseMetaData getDatabaseMetaData() throws SQLException {
		SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();
		@Cleanup Connection connection = sessionImplementor.isConnected() ? sessionImplementor.connection() : sessionImplementor.getJdbcConnectionAccess().obtainConnection(); 
		return connection.getMetaData();
	}
	
	public static ResultSet getDatabaseTables() throws SQLException {
		return getDatabaseMetaData().getTables(null, null, "", new String[]{"TABLE"});
	}
	
	public static void loadDatabaseTableNames() throws SQLException {
		log.debug("开始加载数据库表名信息...");
		ResultSet tables = getDatabaseTables();
		if (tableNames.size() != 0) tableNames.clear();
		while (tables.next()) tableNames.add(tables.getString(3));
		log.debug("数据库表名信息加载完毕...");
	}
}
