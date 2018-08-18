package org.ruanwei.demo.springframework;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.dataAccess.jdbc.SpringJdbcService;
import org.ruanwei.demo.springframework.dataAccess.springdata.SpringDataService;
import org.ruanwei.demo.springframework.dataAccess.tx.SpringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * 
 * @SpringJUnitConfig(AppConfig.class) is composed of
 * @ExtendWith(SpringExtension.class) and ContextConfiguration(classes =
 *                                    AppConfig.class).
 * @SpringJUnitWebConfig also.
 * 
 * 1.避免手动初始化ApplicationContext
 * 2.避免手动获取bean实例
 * 3.避免手动数据库清理
 * 
 * @author ruanwei
 *
 */
// @DirtiesContext
// @Transactional("txManager")
// @Rollback
// @Commit
@ActiveProfiles("development")
@SpringJUnitConfig(AppConfig2.class)
public class DataAccessTest {
	private static Log log = LogFactory.getLog(DataAccessTest.class);

	@Autowired
	private SpringJdbcService springJdbcService;
	@Autowired
	private SpringTransactionService springTransactionService;
	@Autowired
	private SpringDataService springDataService;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	//@Disabled
	@Test
	void testDataAccess() {
		testSpringJdbcService();
		testJdbcTransactionService();
		//testSpringDataService();
	}
	
	@Disabled
	@Test
	void testDataAccess2() {
		testSpringDataService();
	}

	private void testSpringJdbcService() {
		assertNotNull(springJdbcService,
				"springJdbcService is null++++++++++++++++++++++++++++");
		springJdbcService.testSpringJdbc();
	}

	private void testJdbcTransactionService() {
		assertNotNull(springTransactionService,
				"springTransactionService is null++++++++++++++++++++++++++++");
		try {
			springTransactionService.testJdbcTransaction();
		} catch (Exception e) {
			log.error("transaction rolled back", e);
		}
	}

	private void testSpringDataService() {
		assertNotNull(springDataService,
				"springDataService is null++++++++++++++++++++++++++++");
		springDataService.testSpringData();
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
