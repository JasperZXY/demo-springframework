package org.ruanwei.demo.springframework;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.data.jdbc.SpringDataJdbcRepository;
import org.ruanwei.demo.springframework.dataAccess.User;
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
// @Transactional("txManager")
// @Rollback
// @Commit
@ActiveProfiles("development")
//@SpringJUnitConfig(locations = "classpath:spring/applicationContext2.xml")
@SpringJUnitConfig(AppConfig2.class)
public class SpringDataTest {
	private static Log log = LogFactory.getLog(SpringDataTest.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp", 35, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate1 = new User("ruanwei", 18, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate2 = new User("ruanwei_tmp", 88, Date.valueOf("1983-07-06"));

	private static final Map<String, Object> mapParamForCreate1 = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForUpdate1 = new HashMap<String, Object>();
	private static final Map<String, Object> mapParamForUpdate2 = new HashMap<String, Object>();

	private static final int args0 = 0;
	private static final int args1 = 1;
	private static final int args2 = 2;

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

	// see also SimpleJdbcRepository
	@Autowired
	private SpringDataJdbcRepository springDataJdbcRepository;

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
	public void testSpringDataJdbc() {
		testCRUD();
	}

	private void testCRUD() {
		testCreate();
		testBatchUpdate();
		testQueryForSingleRow();
		testQueryForList();
		testDelete();
	}

	private void testCreate() {
		int count = springDataJdbcRepository.createUser(paramForCreate1.getName(), paramForCreate1.getAge(),
				paramForCreate1.getBirthday());
		log.info("count========" + count);

		// see alse saveAll(Iterable<S> entities)
		User user = springDataJdbcRepository.save(paramForCreate1);
		log.info("user========" + user);
	}

	private void testBatchUpdate() {
		int count = springDataJdbcRepository.updateUser("ruanwei_tmp", 18);
		log.info("count========" + count);
	}

	private void testQueryForSingleRow() {
		String name = springDataJdbcRepository.findNameById(args1);
		log.info("name========" + name);

		Map<String, Object> columnMap = springDataJdbcRepository.findNameAndAgeById(args1);
		columnMap.forEach((k, v) -> log.info(k + "=" + v));

		User user = springDataJdbcRepository.findUserById(args1);
		log.info("user========" + user);

		// see also findAll()
		Optional<User> user2 = springDataJdbcRepository.findById(args1);
		log.info("user2========" + user2.get());
	}

	private void testQueryForList() {
		List<String> nameList = springDataJdbcRepository.findNameListById(args0);
		nameList.forEach(e -> log.info("name========" + e));

		List<Map<String, Object>> columnMapList = springDataJdbcRepository.findNameAndAgeListById(args0);
		columnMapList.forEach(columbMap -> columbMap.forEach((k, v) -> log.info(k + "=" + v)));

		List<User> userList = springDataJdbcRepository.findUserListById(args0);
		userList.forEach(e -> log.info("user========" + e));
	}

	private void testDelete() {
		int count = springDataJdbcRepository.deleteUser(args2);
		log.info("count========" + count);

		// see also delete(entity) and deleteAll()
		springDataJdbcRepository.deleteById(args2);
	}

	@Disabled
	@Test
	public void testSpringDataJpa() {
	}

	@Disabled
	@Test
	public void testSpringDataRedis() {
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
