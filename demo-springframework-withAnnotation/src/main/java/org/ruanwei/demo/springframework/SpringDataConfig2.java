package org.ruanwei.demo.springframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Import(JdbcConfiguration.class)
@EnableJdbcRepositories
@Configuration
public class SpringDataConfig2 {
	private static Log log = LogFactory.getLog(SpringDataConfig2.class);

	@Autowired
	private DataAccessConfig2 dataAccessConfig2;

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataAccessConfig2.dataSource1());
	}
	
}
