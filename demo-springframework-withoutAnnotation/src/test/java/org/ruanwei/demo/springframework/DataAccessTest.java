package org.ruanwei.demo.springframework;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDao;
import org.ruanwei.demo.springframework.dataAccess.jdbc.User;
import org.ruanwei.demo.springframework.dataAccess.tx.JdbcTransaction;
import org.springframework.beans.factory.annotation.Autowired;
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
// @Rollback
// @Commit
// @Transactional("txManager")
@ActiveProfiles("development")
//@SpringJUnitConfig(locations = "classpath:spring/applicationContext.xml")
@SpringJUnitConfig(AppConfig.class)
public class DataAccessTest {
	private static Log log = LogFactory.getLog(DataAccessTest.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp", 35, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate1 = new User("ruanwei", 18, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate2 = new User("ruanwei_tmp", 88, Date.valueOf("1983-07-06"));

	private static final Map<String, Object> mapParamForCreate1 = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForUpdate1 = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForUpdate2 = new HashMap<String, Object>();

	private static final int args0 = 0;
	private static final int args1 = 1;

	static {
		mapParamForCreate1.put("name", "ruanwei_tmp");
		mapParamForCreate1.put("age", 35);
		mapParamForCreate1.put("birthday", Date.valueOf("1983-07-06"));

		mapParamForUpdate1.put("name", "ruanwei");
		mapParamForUpdate1.put("age", 18);
		mapParamForUpdate1.put("birthday", Date.valueOf("1983-07-06"));

		mapParamForUpdate2.put("name", "ruanwei_tmp");
		mapParamForUpdate2.put("age", 88);
		mapParamForUpdate2.put("birthday", Date.valueOf("1983-07-06"));
	}

	@Autowired
	private JdbcDao jdbcDao;

	@Autowired
	private JdbcTransaction jdbcTransaction;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	// @Disabled
	@Test
	void testSpringJdbc() {
		assertNotNull(jdbcDao, "jdbcDao is null++++++++++++++++++++++++++++");
		testCRUD();
	}

	// @Disabled
	@Test
	void testSpringJdbcWithTransaction() {
		assertNotNull(jdbcTransaction, "jdbcTransaction is null++++++++++++++++++++++++++++");
		try {
			jdbcTransaction.transactionalMethod();
		} catch (Exception e) {
			log.error("transaction rolled back", e);
		}
	}

	@AfterEach
	void afterEach() {
		log.info("afterEach()");
	}

	@AfterAll
	static void afterAll() {
		log.info("afterAll()");
	}

	private void testCRUD() {
		testCreate();
		testBatchUpdate();
		testQueryForSingleRow();
		testQueryForList();
		testDelete();
	}

	private void testCreate() {
		jdbcDao.createUser1(paramForCreate1);
		jdbcDao.createUser2(paramForCreate1);
		jdbcDao.createUser3(paramForCreate1);
		jdbcDao.createUser4(paramForCreate1);
		jdbcDao.createUser4(mapParamForCreate1);
		jdbcDao.createUser5(paramForCreate1);
		jdbcDao.createUser5(mapParamForCreate1);
	}

	private void testBatchUpdate() {
		List<User> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDao.batchUpdateUser1(users);
		jdbcDao.batchUpdateUser2(users);
		jdbcDao.batchUpdateUser3(users);
		jdbcDao.batchUpdateUser4(mapParamForUpdate1, mapParamForUpdate2);
	}

	private void testQueryForSingleRow() {
		jdbcDao.queryForSingleRowWithSingleColumn(args1);
		jdbcDao.queryForSingleRowAsColumnMap(args1);
		jdbcDao.queryForSingleRowAsBeanProperty(args1);
	}

	private void testQueryForList() {
		jdbcDao.queryForListWithSingleColumn(args0);
		jdbcDao.queryForListWithColumnMap(args0);
		jdbcDao.queryForListWithBeanProperty(args0);
	}

	private void testDelete() {
		jdbcDao.deleteUser(2);
	}
}
