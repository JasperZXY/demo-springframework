package org.ruanwei.demo.springframework;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.cj.xdevapi.SessionFactory;

@EnableTransactionManagement
@PropertySource("classpath:jdbc.properties")
@Configuration
public class DataAccessConfig2 {// implements TransactionManagementConfigurer {
	private static Log log = LogFactory.getLog(DataAccessConfig2.class);

	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;

	// ==========A.Data Access:JDBC==========
	@Qualifier("embeddedDataSource")
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.HSQL)
				.setScriptEncoding("UTF-8").ignoreFailedDrops(true).addScript("classpath:db/db-schema-hsql.sql")
				.addScripts("classpath:db/db-test-data.sql").build();
	}

	// DataSource:plain JDBC
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
	@Bean(destroyMethod = "close")
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
	@Bean(destroyMethod = "close")
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
	// local transaction manager for plain JDBC
	@Primary
	@Bean("transactionManager")
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource1());
		return transactionManager;
	}

	// local transaction manager for JPA
	@Bean("jpaTransactionManager")
	public PlatformTransactionManager jpaTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(dataSource1());
		return jpaTransactionManager;
	}

	// local transaction manager for Hibernate
	@Bean("hibernateTransactionManager")
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		//hibernateTransactionManager.setSessionFactory(sessionFactory());
		return hibernateTransactionManager;
	}
	
	//@Bean("sessionFactory")
	public SessionFactory sessionFactory() {
		SessionFactory sessionFactory = new SessionFactory();
		return sessionFactory;
	}

	// global transaction manager
	@Lazy
	@Bean("globalTxManager")
	public PlatformTransactionManager globalTxManager() {
		JtaTransactionManager txManager = new JtaTransactionManager();
		return txManager;
	}

	// The valid phases are BEFORE_COMMIT, AFTER_COMMIT (default), AFTER_ROLLBACK
	// and AFTER_COMPLETION.
	@TransactionalEventListener
	public void handleTransactionalEvent(ApplicationEvent event) {
		log.info("handleTransactionalEvent======" + event);
	}

}
