package org.ruanwei.demo.springframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

//@Import(JdbcConfiguration.class)
//@EnableJdbcRepositories
@PropertySource("classpath:jdbc.properties")
@Configuration
public class SpringDataConfig implements EnvironmentAware, InitializingBean {// implements
																				// TransactionManagementConfigurer
																				// {
	private static Log log = LogFactory.getLog(SpringDataConfig.class);

	private String driverClassName;
	private String url;
	private String username;
	private String password;

	private Environment env;

	public SpringDataConfig() {
		log.info("SpringDataConfig()======");
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

}
