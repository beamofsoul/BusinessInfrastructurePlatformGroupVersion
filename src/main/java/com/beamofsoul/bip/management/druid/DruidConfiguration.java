package com.beamofsoul.bip.management.druid;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Druid DataSource配置
 * 通过读取application.yml文件中关于spring.datasource.*的所有配置
 * 由于SpringBoot 1.4.0以后不支持不选择数据库连接池的默认配置
 * 在数据库连接池参数属性中必须指定数据库连接池的名称
 * 所以需要对Druid数据源与连接池进行手动Java配置
 * @author MingshuJian
 */
@Slf4j
@Configuration
public class DruidConfiguration {
	
//	@Value("${spring.datasource.type}")
//	private String dbType;

	@Value("${spring.datasource.url}")  
    private String dbUrl;  
      
    @Value("${spring.datasource.username}")  
    private String username;  
      
    @Value("${spring.datasource.password}")  
    private String password;  
      
    @Value("${spring.datasource.driverClassName}")  
    private String driverClassName;  
      
    @Value("${spring.datasource.initialSize}")  
    private int initialSize;  
      
    @Value("${spring.datasource.maxActive}")  
    private int maxActive;
    
    @Value("${spring.datasource.minIdle}")  
    private int minIdle;
      
    @Value("${spring.datasource.maxWait}")  
    private int maxWait;

    @Value("${spring.datasource.removeAbandoned}")
    private boolean removeAbandoned;
    
    @Value("${spring.datasource.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;
    
    @Value("${spring.datasource.logAbandoned}")
    private boolean logAbandoned;
    
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")  
    private int timeBetweenEvictionRunsMillis;  
      
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")  
    private int minEvictableIdleTimeMillis;  
      
    @Value("${spring.datasource.validationQuery}")  
    private String validationQuery;  
      
    @Value("${spring.datasource.testWhileIdle}")  
    private boolean testWhileIdle;  
      
    @Value("${spring.datasource.testOnBorrow}")  
    private boolean testOnBorrow;  
      
    @Value("${spring.datasource.testOnReturn}")  
    private boolean testOnReturn;  
      
    @Value("${spring.datasource.poolPreparedStatements}")  
    private boolean poolPreparedStatements;  
      
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")  
    private int maxPoolPreparedStatementPerConnectionSize;  
      
    @Value("${spring.datasource.filters}")  
    private String filters;  

    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;
    
    @Value("${spring.datasource.useGlobalDataSourceStat}")
    private boolean useGlobalDataSourceStat;
    
    @Bean
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource  
    public DataSource dataSource(){  
        DruidDataSource datasource = new DruidDataSource();
        
//        datasource.setDbType(dbType);
        datasource.setUrl(this.dbUrl);  
        datasource.setUsername(username);  
        datasource.setPassword(password);  
        datasource.setDriverClassName(driverClassName);  
          
        datasource.setInitialSize(initialSize);  
        datasource.setMinIdle(minIdle);  
        datasource.setMaxActive(maxActive);  
        datasource.setMaxWait(maxWait);  
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        datasource.setValidationQuery(validationQuery);  
        datasource.setTestWhileIdle(testWhileIdle);  
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);  
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);  
        
        datasource.setRemoveAbandoned(removeAbandoned);
        datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        datasource.setLogAbandoned(logAbandoned);
        
        try {
            datasource.setFilters(filters);  
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);  
        }
        
        datasource.setConnectionProperties(connectionProperties);
        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        
        return datasource;
    }  
}
