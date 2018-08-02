package org.ruanwei.demo.springframework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ActiveProfiles("development")
@SpringJUnitConfig(AppConfig2.class)
public class DemoTest {
	private static Log log = LogFactory.getLog(ContextTest.class);

	// @Autowired
	@Resource(name = "dataSource")
	private DataSource dataSource;

	private static EmbeddedDatabase db;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");
		// creates an HSQL in-memory database populated from default scripts
		// classpath:schema.sql and classpath:data.sql
		db = new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.H2).setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true)
				.addScript("classpath:db/db-schema.sql")
				.addScripts("classpath:db/db-test-data.sql").build();
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	@Disabled
	@Test
	void testEmbeddedDatabase() {
		log.info("dataSource++++++++++++++++++++++++++++" + dataSource);
		assertNotNull(dataSource,
				"dataSource is null++++++++++++++++++++++++++++");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int count = jdbcTemplate.queryForObject("select count(*) from user",
				Integer.class);
		assertEquals(5, count, "count is not equal++++++++++++++++++++++++++++");
	}

	@Test
	void testEmbeddedDatabase2() {
		assertNotNull(db, "db is null++++++++++++++++++++++++++++");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
		int count = jdbcTemplate.queryForObject("select count(*) from user",
				Integer.class);
		assertEquals(5, count, "count is not equal++++++++++++++++++++++++++++");
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
