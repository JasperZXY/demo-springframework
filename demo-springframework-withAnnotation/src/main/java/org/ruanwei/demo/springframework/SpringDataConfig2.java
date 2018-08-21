package org.ruanwei.demo.springframework;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//@Import(JdbcConfiguration.class)
//@EnableJdbcRepositories
@PropertySource("classpath:jdbc.properties")
@Configuration
public class SpringDataConfig2 implements EnvironmentAware, InitializingBean {// implements
																				// TransactionManagementConfigurer
																				// {
	private static Log log = LogFactory.getLog(SpringDataConfig2.class);

	private String driverClassName;
	private String url;
	private String username;
	private String password;

	private Environment env;

	public SpringDataConfig2() {
		log.info("DataAccessConfig()======");
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("afterPropertiesSet()======" + env);
		driverClassName = env.getProperty("jdbc.driverClassName",
				"com.mysql.cj.jdbc.Driver");
		url = env
				.getProperty(
						"jdbc.url",
						"jdbc:mysql://localhost:3306/demo?useUnicode=true&autoReconnect=true&characterEncoding=utf-8");
		username = env.getProperty("jdbc.username", "root");
		password = env.getProperty("jdbc.password", "qqqq1234");
	}

	// ==========A.Data Access:JDBC==========
	@Qualifier("embeddedDataSource")
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().generateUniqueName(true)
				.setType(EmbeddedDatabaseType.HSQL).setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true)
				.addScript("classpath:db/db-schema-hsql.sql")
				.addScripts("classpath:db/db-test-data.sql").build();
	}

	// DataSource:pure jdbc
	// should only be used for testing purposes since no pooling.
	@Primary
	@Qualifier("jdbcDataSource")
	@Bean
	public DataSource dataSource1() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	// polled-DataSource:dbcp2, see PoolingDataSource
	@Qualifier("dbcp2DataSource")
	@Bean(destroyMethod="close")
	public DataSource dataSource2() {
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

	// polled-DataSource:c3p0
	@Qualifier("c3p0DataSource")
	@Bean(destroyMethod="close")
	public DataSource dataSource3() throws Exception {
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

	// ==========A.Data Access:TransactionManager==========
	// local transaction manager for jdbc
	@Primary
	@Bean("txManager")
	public PlatformTransactionManager txManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource1());
		return txManager;
	}

	// global transaction manager
	@Lazy
	@Bean("globalTxManager")
	public PlatformTransactionManager globalTxManager() {
		JtaTransactionManager txManager = new JtaTransactionManager();
		return txManager;
	}

}
