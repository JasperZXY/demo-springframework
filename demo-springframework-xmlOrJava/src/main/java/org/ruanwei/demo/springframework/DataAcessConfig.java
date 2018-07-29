package org.ruanwei.demo.springframework;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class DataAcessConfig {

	@Bean
	public DataSource dataSource1(
			@Value("${jdbc.driverClassName}") String driverClassName,
			@Value("${jdbc.url}") String url,
			@Value("${jdbc.username}") String username,
			@Value("${jdbc.password}") String password) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	// PoolingDataSource
	@Bean
	public DataSource dataSource2(
			@Value("${jdbc.driverClassName}") String driverClassName,
			@Value("${jdbc.url}") String url,
			@Value("${jdbc.username}") String username,
			@Value("${jdbc.password}") String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		dataSource.setInitialSize(10);
		dataSource.setMaxTotal(100);
		dataSource.setMinIdle(3);
		dataSource.setMaxIdle(10);
		return dataSource;
	}

	@Bean
	public DataSource dataSource3(
			@Value("${jdbc.driverClassName}") String driverClassName,
			@Value("${jdbc.url}") String url,
			@Value("${jdbc.username}") String username,
			@Value("${jdbc.password}") String password) throws Exception {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClassName);
		dataSource.setJdbcUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setInitialPoolSize(10);
		dataSource.setMinPoolSize(10);
		dataSource.setMaxPoolSize(100);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager txManager(
			@Value("#{dataSource1}") DataSource dataSource) {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}
	
	@Lazy
	@Bean
	public PlatformTransactionManager globalTxManager() {
		JtaTransactionManager txManager = new JtaTransactionManager();
		return txManager;
	}
	
	@Bean
	public JdbcDAO jdbcDAO(@Value("#{dataSource1}") DataSource dataSource) {
		JdbcDAO jdbcDAO = new JdbcDAO();
		jdbcDAO.setDataSource(dataSource);
		return jdbcDAO;
	}
}
