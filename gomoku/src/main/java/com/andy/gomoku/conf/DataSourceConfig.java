package com.andy.gomoku.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfig {

	@Value("${datasource.url}")
	private String url;

	@Value("${datasource.username}")
	private String user;

	@Value("${datasource.password}")
	private String password;

	private String driverClass = "com.mysql.jdbc.Driver";
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		if(driverClass.startsWith("$")){
			if(url.startsWith("jdbc:oracle")){
				driverClass = "oracle.jdbc.dirver.OracleDriver";
			}else {
				driverClass = "com.mysql.jdbc.Driver";
			}
		}
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		initDataSource(dataSource);
		return dataSource;
	}
	
	private void initDataSource(DruidDataSource dataSource) {
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(1);
		dataSource.setMaxWait(60000);
		dataSource.setMinIdle(1);
		dataSource.setTimeBetweenEvictionRunsMillis(3000);
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("SELECT 'x' FROM DUAL");
		dataSource.setValidationQueryTimeout(5);
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
	}

}
