package org.ruanwei.demo.springframework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ActiveProfiles("development")
@SpringJUnitConfig(AppConfig2.class)
public class DemoTest {
	private static Log log = LogFactory.getLog(ContextTest.class);
	
	//@Autowired
	@Resource(name="dataSource")
	private DataSource dataSource;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	@Test
	void testEmbeddedDatabase() {
		log.info("dataSource++++++++++++++++++++++++++++"+dataSource);
		assertNotNull(dataSource, "dataSource is null++++++++++++++++++++++++++++");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int count = jdbcTemplate.queryForObject("select count(*) from user", Integer.class);
		assertEquals(count,5,"count is not equal++++++++++++++++++++++++++++");
	}

	@AfterEach
	void afterEach() {
		log.info("afterEach()");
	}

	@AfterAll
	static void afterAll() {
		log.info("afterAll()");
	}
}
