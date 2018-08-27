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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.dataAccess.User;
import org.ruanwei.demo.springframework.dataAccess.jdbc.UserJdbcDao;
import org.ruanwei.demo.springframework.dataAccess.orm.hibernate.UserHibernateDao;
import org.ruanwei.demo.springframework.dataAccess.orm.jpa.UserJpaDao;
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
//@SpringJUnitConfig(locations = "classpath:spring/applicationContext2.xml")
@SpringJUnitConfig(AppConfig2.class)
public class DataAccessTest {
	private static Log log = LogFactory.getLog(DataAccessTest.class);

	private static final String update_sql_21 = "update user set age = ? where name = ?";
	private static final String update_sql_22 = "update user set age = :age where name = :name";

	private static final String select_sql_11 = "select * from user where id = ?";
	private static final String select_sql_12 = "select * from user where id = :id";

	private static final String select_sql_21 = "select name, age from user where id = ?";
	private static final String select_sql_22 = "select name, age from user where id = :id";

	private static final String select_sql_31 = "select * from user where name = ?";
	private static final String select_sql_32 = "select * from user where name = :name";

	private static final String select_sql_41 = "select name, age from user where name = ?";
	private static final String select_sql_42 = "select name, age from user where name = :name";

	private static final String delete_sql_11 = "delete from user where name = ?";
	private static final String delete_sql_12 = "delete from user where name = :name";

	private static final User paramForCreate = new User("ruanwei_tmp", 35, Date.valueOf("1983-07-06"));
	private static final User[] arrayParamForBatchCreate = new User[] { paramForCreate };
	private static final List<User> listParamForCreate = Arrays.asList(arrayParamForBatchCreate);

	private static final User paramForUpdate = new User("ruanwei_tmp", 18, Date.valueOf("1983-07-06"));
	private static final User[] arrayParamForbatchUpdate = new User[] { paramForUpdate };
	private static final List<User> listParamForUpdate = Arrays.asList(arrayParamForbatchUpdate);

	private static final User paramForDelete = new User("ruanwei_tmp", 18, Date.valueOf("1983-07-06"));
	private static final User[] arrayParamForBatchDelete = new User[] { paramForDelete };
	private static final List<User> listParamForDelete = Arrays.asList(arrayParamForBatchDelete);

	private static final Map<String, Object> mapParamForCreate = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForUpdate = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForQuery1 = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForQuery2 = new HashMap<String, Object>();

	private static final Object[] arrayParamForUpdate = new Object[] { "ruanwei_tmp", 18 };
	private static final Object[] arrayParamForQuery1 = new Object[] { 1 };
	private static final Object[] arrayParamForQuery2 = new Object[] { "ruanwei_tmp" };

	private static final int id0 = 0;
	private static final int id1 = 1;
	private static final int id3 = 3;

	static {
		mapParamForCreate.put("name", "ruanwei_tmp");
		mapParamForCreate.put("age", 35);
		mapParamForCreate.put("birthday", Date.valueOf("1983-07-06"));

		mapParamForUpdate.put("name", "ruanwei_tmp");
		mapParamForUpdate.put("age", 18);

		mapParamForQuery1.put("id", 1);

		mapParamForQuery2.put("name", "ruanwei_tmp");
	}

	@Autowired
	private UserJdbcDao userJdbcDao;

	@Autowired
	private UserHibernateDao userHibernateDao;

	// @Autowired
	private UserJpaDao userJpaDao;

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
	void testJdbcDao() {
		assertNotNull(userJdbcDao, "userJdbcDao is null++++++++++++++++++++++++++++");
		testCRUD();
	}

	// @Disabled
	@Test
	void testHibernateDao() {
		assertNotNull(userHibernateDao, "userHibernateDao is null++++++++++++++++++++++++++++");
		userHibernateDao.findAll();
	}

	@Disabled
	@Test
	void testJpaDao() {
		assertNotNull(userJpaDao, "userJpaDao is null++++++++++++++++++++++++++++");
		userJpaDao.findAll();
	}

	@Disabled
	@Test
	void testJdbcDaoWithTransaction() {
		assertNotNull(userJdbcDao, "userJdbcDao is null++++++++++++++++++++++++++++");
		try {
			userJdbcDao.transactionalMethod(arrayParamForBatchCreate);
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
		testUpdate();
		testQueryForSingleRow();
		testQueryForList();
		testDelete();
	}

	private void testCreate() {
		userJdbcDao.save(paramForCreate);
		userJdbcDao.saveAll(listParamForCreate);
	}

	private void testUpdate() {
		userJdbcDao.updateByExample(update_sql_21, arrayParamForUpdate);
		userJdbcDao.updateByExample(update_sql_22, mapParamForUpdate);
		userJdbcDao.updateByExample2(update_sql_22, mapParamForUpdate);

//		userJdbcDao.batchUpdateByExample(sql, batchArgs);
//		userJdbcDao.batchUpdateByExample(sql, batchArgs);
//		userJdbcDao.batchUpdateByExample(sql, batchArgs, batchSize, ppss);
	}

	private void testQueryForSingleRow() {
		userJdbcDao.findById(id1);
		userJdbcDao.findById2(id1);

		userJdbcDao.existsById(id1);
		userJdbcDao.existsById(id1);

		userJdbcDao.count();

		userJdbcDao.findByExample(select_sql_11, arrayParamForQuery1);
		userJdbcDao.findByExample(select_sql_12, mapParamForQuery1);

		userJdbcDao.findByExampleAsMap(select_sql_21, arrayParamForQuery1);
		userJdbcDao.findByExampleAsMap(select_sql_22, mapParamForQuery1);
	}

	private void testQueryForList() {
		userJdbcDao.findAll();
		// userJdbcDao.findAllById(ids);

		userJdbcDao.findAllByExample(select_sql_31, arrayParamForQuery2);
		userJdbcDao.findAllByExample(select_sql_32, mapParamForQuery2);

		userJdbcDao.findAllByExampleAsMap(select_sql_41, arrayParamForQuery2);
		userJdbcDao.findAllByExampleAsMap(select_sql_42, mapParamForQuery2);
	}

	private void testDelete() {
		userJdbcDao.deleteById(id3);
		userJdbcDao.deleteById2(id3);

		userJdbcDao.delete(paramForDelete);
		userJdbcDao.delete2(paramForDelete);

		userJdbcDao.deleteAll(listParamForDelete);

		userJdbcDao.deleteAll();

		userJdbcDao.updateByExample(delete_sql_11, mapParamForQuery1);
		userJdbcDao.updateByExample(delete_sql_12, mapParamForQuery2);
	}
}
