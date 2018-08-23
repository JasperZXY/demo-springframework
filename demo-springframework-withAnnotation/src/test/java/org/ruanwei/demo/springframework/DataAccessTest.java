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
import org.ruanwei.demo.springframework.dataAccess.User;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.ruanwei.demo.springframework.dataAccess.tx.JdbcTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

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
//@SpringJUnitConfig(locations = "classpath:spring/applicationContext2.xml")
@SpringJUnitConfig(AppConfig2.class)
public class DataAccessTest {
	private static Log log = LogFactory.getLog(DataAccessTest.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp", 35, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate1 = new User("ruanwei", 18, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate2 = new User("ruanwei_tmp", 88, Date.valueOf("1983-07-06"));

	private static final Map<String, Object> paramForCreate2 = new HashMap<String, Object>();
	private static final Map<String, Object> paramForUpdate3 = new HashMap<String, Object>();
	private static final Map<String, Object> paramForUpdate4 = new HashMap<String, Object>();

	static {
		paramForCreate2.put("name", "ruanwei_tmp");
		paramForCreate2.put("age", 35);
		paramForCreate2.put("birthday", Date.valueOf("1983-07-06"));

		paramForUpdate3.put("name", "ruanwei");
		paramForUpdate3.put("age", 18);
		paramForUpdate3.put("birthday", Date.valueOf("1983-07-06"));

		paramForUpdate4.put("name", "ruanwei_tmp");
		paramForUpdate4.put("age", 88);
		paramForUpdate4.put("birthday", Date.valueOf("1983-07-06"));
	}

	@Autowired
	private JdbcDAO jdbcDAO;
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
		assertNotNull(jdbcDAO, "jdbcDAO is null++++++++++++++++++++++++++++");
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
		testQueryFormultiRow();
		testDelete();
	}

	private void testCreate() {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser2(paramForCreate1);
		jdbcDAO.createUser3(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate2);
		jdbcDAO.createUser5(paramForCreate1);
		jdbcDAO.createUser5(paramForCreate2);
	}

	private void testBatchUpdate() {
		List<User> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDAO.batchUpdateUser1(users);
		jdbcDAO.batchUpdateUser2(users);
		jdbcDAO.batchUpdateUser3(users);
		jdbcDAO.batchUpdateUser4(paramForUpdate3, paramForUpdate4);
	}

	private void testQueryForSingleRow() {
		jdbcDAO.queryForSingleColumn();
		jdbcDAO.queryForMultiColumn();
		jdbcDAO.queryForObject();
	}

	private void testQueryFormultiRow() {
		jdbcDAO.queryForSingleColumnList();
		jdbcDAO.queryForMultiColumnList();
		jdbcDAO.queryForObjectList();
	}

	private void testDelete() {
		jdbcDAO.deleteUser(2);
	}
}
