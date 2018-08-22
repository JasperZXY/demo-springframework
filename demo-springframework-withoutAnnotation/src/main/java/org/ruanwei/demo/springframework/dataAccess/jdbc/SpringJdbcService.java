package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpringJdbcService {
	private static Log log = LogFactory.getLog(SpringJdbcService.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp", 35,
			Date.valueOf("1983-07-06"));
	private static final User paramForUpdate1 = new User("ruanwei", 18,
			Date.valueOf("1983-07-06"));
	private static final User paramForUpdate2 = new User("ruanwei_tmp", 88,
			Date.valueOf("1983-07-06"));

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

	private JdbcDao jdbcDao;

	public void testSpringJdbc() {
		testCRUD();
	}

	private void testCRUD() {
		testCreate();
		testBatchUpdate();
		testQueryForSingleRow();
		testQueryFormultiRow();
		testDelete();
	}

	private void testCreate() {
		jdbcDao.createUser1(paramForCreate1);
		jdbcDao.createUser2(paramForCreate1);
		jdbcDao.createUser3(paramForCreate1);
		jdbcDao.createUser4(paramForCreate1);
		jdbcDao.createUser4(paramForCreate2);
		jdbcDao.createUser5(paramForCreate1);
		jdbcDao.createUser5(paramForCreate2);
	}

	private void testBatchUpdate() {
		List<User> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDao.batchUpdateUser1(users);
		jdbcDao.batchUpdateUser2(users);
		jdbcDao.batchUpdateUser3(users);
		jdbcDao.batchUpdateUser4(paramForUpdate3, paramForUpdate4);
	}

	private void testQueryForSingleRow() {
		jdbcDao.queryForSingleColumn();
		jdbcDao.queryForMultiColumn();
		jdbcDao.queryForObject();
	}

	private void testQueryFormultiRow() {
		jdbcDao.queryForSingleColumnList();
		jdbcDao.queryForMultiColumnList();
		jdbcDao.queryForObjectList();
	}

	private void testDelete() {
		// jdbcDAO.deleteUser(2);
	}

	public void setJdbcDAO(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}

}
