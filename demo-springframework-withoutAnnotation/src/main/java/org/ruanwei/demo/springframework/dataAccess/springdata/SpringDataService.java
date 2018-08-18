package org.ruanwei.demo.springframework.dataAccess.springdata;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.JdbcRepository;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.User2;

public class SpringDataService {
	private static Log log = LogFactory.getLog(SpringDataService.class);
	
	private static final User2 paramForCreate = new User2("ruanwei_tmp", 35,
			Date.valueOf("1983-07-06"));

	// TODO:无法获取实例
	private JdbcRepository jdbcRepository;

	public void testSpringData() {
		// testSpringDataJdbc();
		testSpringDataRedis();
	}

	public void testSpringDataJdbc() {
		jdbcRepository.save(paramForCreate);
	}

	public void testSpringDataRedis() {
	}
}
