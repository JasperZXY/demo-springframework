package org.ruanwei.demo.springframework;

import java.sql.Date;
import java.util.Arrays;
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
import org.ruanwei.demo.springframework.data.jdbc.JdbcCrudRepository;
import org.ruanwei.demo.springframework.data.jdbc.JdbcPagingAndSortingRepository;
import org.ruanwei.demo.springframework.data.jdbc.JdbcRepository;
import org.ruanwei.demo.springframework.dataAccess.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	private static final User paramForCreate2 = new User("ruanwei_tmp", 88, Date.valueOf("1983-07-06"));
	private static final User paramForUpdate = new User("ruanwei_tmp", 18, Date.valueOf("1983-07-06"));
	private static final User paramForDelete = new User("ruanwei_tmp", 18, Date.valueOf("1983-07-06"));
	private static final List<User> listParamForCreate = Arrays.asList(paramForCreate1, paramForCreate2);
	private static final List<User> listParamForDelete = Arrays.asList(paramForDelete);

	private static final int args0 = 0;
	private static final int args1 = 1;
	private static final int args2 = 2;
	private static final int args3 = 3;
	private static final List<Integer> listParamForQuery = Arrays.asList(args0, args1, args2, args3);

	@Autowired
	private JdbcRepository jdbcRepository;

	@Autowired
	private JdbcCrudRepository jdbcCrudRepository;

	@Autowired
	private JdbcPagingAndSortingRepository jdbcPagingAndSortingRepository;

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
		testUpdate();
		testQueryForSingleRow();
		testQueryForList();
		//testQueryForPagingAndSorting();
		testDelete();
	}

	private void testCreate() {
		int count = jdbcRepository.createUser(paramForCreate1.getName(), paramForCreate1.getAge(),
				paramForCreate1.getBirthday());
		log.info("jdbcRepository.createUser========" + count);

		User user = jdbcCrudRepository.save(paramForCreate1);
		log.info("jdbcCrudRepository.save========" + user);

		Iterable<User> userList = jdbcCrudRepository.saveAll(listParamForCreate);
		userList.forEach(e -> log.info("jdbcCrudRepository.saveAll========" + e));
	}

	private void testUpdate() {
		int count = jdbcRepository.updateUser(paramForUpdate.getName(), paramForUpdate.getAge());
		log.info("jdbcRepository.updateUser========" + count);
	}

	private void testQueryForSingleRow() {
		String name = jdbcRepository.findNameById(args1);
		log.info("jdbcRepository.findNameById========" + name);

		Map<String, Object> columnMap = jdbcRepository.findNameAndAgeById(args1);
		columnMap.forEach((k, v) -> log.info("jdbcRepository.findNameAndAgeById====" + k + "=" + v));

		User user = jdbcRepository.findUserById(args1);
		log.info("jdbcRepository.findUserById========" + user);

		Optional<User> user2 = jdbcCrudRepository.findById(args1);
		log.info("jdbcCrudRepository.findById========" + user2.get());

		long count = jdbcCrudRepository.count();
		log.info("jdbcCrudRepository.count()========" + count);

		boolean exist = jdbcCrudRepository.existsById(args1);
		log.info("jdbcCrudRepository.existsById========" + exist);
	}

	private void testQueryForList() {
		List<String> nameList = jdbcRepository.findNameListById(args0);
		nameList.forEach(e -> log.info("jdbcRepository.findNameListById========" + e));

		List<Map<String, Object>> columnMapList = jdbcRepository.findNameAndAgeListById(args0);
		columnMapList.forEach(columbMap -> columbMap
				.forEach((k, v) -> log.info("jdbcRepository.findNameAndAgeListById====" + k + "=" + v)));

		List<User> userList = jdbcRepository.findUserListById(args0);
		userList.forEach(e -> log.info("jdbcRepository.findUserListById========" + e));

		Iterable<User> userList2 = jdbcCrudRepository.findAllById(listParamForQuery);
		userList2.forEach(e -> log.info("jdbcCrudRepository.findAllById========" + e));

		Iterable<User> userList3 = jdbcCrudRepository.findAll();
		userList3.forEach(e -> log.info("jdbcCrudRepository.findAll()========" + e));
	}

	private void testQueryForPagingAndSorting() {
		Sort sort = Sort.by(Direction.ASC, "age");
		Pageable pageable = PageRequest.of(0, 5);
		Pageable pageableWithSort = PageRequest.of(0, 5, sort);

		Iterable<User> userList = jdbcPagingAndSortingRepository.findAll(sort);
		userList.forEach(e -> log.info("jdbcPagingAndSortingRepository.findAll========" + e));

		Page<User> userPage = jdbcPagingAndSortingRepository.findAll(pageable);
		userPage.forEach(e -> log.info("jdbcPagingAndSortingRepository.findAll========" + e));

		Page<User> userPage2 = jdbcPagingAndSortingRepository.findAll(pageableWithSort);
		userPage2.forEach(e -> log.info("jdbcPagingAndSortingRepository.findAll========" + e));
	}

	private void testDelete() {
		int count = jdbcRepository.deleteUser(args2);
		log.info("jdbcRepository.deleteUser========" + count);

		jdbcCrudRepository.deleteById(args3);
		jdbcCrudRepository.delete(paramForDelete);
		jdbcCrudRepository.deleteAll(listParamForDelete);
		// jdbcCrudRepository.deleteAll();
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
