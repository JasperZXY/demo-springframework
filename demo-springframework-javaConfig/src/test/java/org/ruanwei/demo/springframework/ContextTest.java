package org.ruanwei.demo.springframework;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.core.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @SpringJUnitConfig(AppConfig.class) is composed of
 * @ExtendWith(SpringExtension.class) and ContextConfiguration(classes =
 *                                    AppConfig.class).
 * @SpringJUnitWebConfig also.
 * @author ruanwei
 *
 */
@Transactional("txManager")
@ActiveProfiles("development")
@SpringJUnitConfig(AppConfig2.class)
public class ContextTest {
	private static Log log = LogFactory.getLog(ContextTest.class);

	@Autowired
	private ContextService contextService;

	@Autowired
	private ApplicationContext context;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");

		MockEnvironment env = new MockEnvironment();
		env.setActiveProfiles("development");
		env.setDefaultProfiles("production");
		env.setProperty("foo", "bar");

		MockPropertySource ps = new MockPropertySource();
		ps.setProperty("foo1", "bar1");
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	@Test
	void testApplicationContext() {
		assertNotNull(context, "context is null++++++++++++++++++++++++++++");
		contextService.setContext(context);
		contextService.testApplicationContext();
		assertNotNull(null,"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
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
