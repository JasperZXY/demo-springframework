package org.ruanwei.demo.springframework;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ruanwei.demo.springframework.dataAccess.jdbc.User;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.User2;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.UserJdbcRepository;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
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
@SpringJUnitConfig(locations = "classpath:spring/applicationContext2.xml")
//@SpringJUnitConfig(AppConfig2.class)
public class SpringDataTest {
	private static Log log = LogFactory.getLog(SpringDataTest.class);

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

	// @Autowired
	private UserJdbcRepository userJdbcRepository;
	// @Autowired
	private SimpleJdbcRepository<User2, Integer> simpleJdbcRepository;

	@BeforeAll
	static void beforeAll() {
		log.info("beforeAll()");
	}

	@BeforeEach
	void beforeEach() {
		log.info("beforeEach()");
	}

	@Disabled
	@Test
	public void testSpringDataJdbc() {
		Iterable<User2> users = userJdbcRepository.findAll();
		users.forEach(u -> log.info("user2========" + u));
		// Iterable<User2> users2 = simpleJdbcRepository.findAll();
		// users2.forEach(u -> log.info("user2=" + u));
		// userJdbcRepository.save(paramForCreate);
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
