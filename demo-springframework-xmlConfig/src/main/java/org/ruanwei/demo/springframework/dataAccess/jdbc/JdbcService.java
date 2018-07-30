package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.User;

public class JdbcService {
	private static Log log = LogFactory.getLog(JdbcService.class);

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

	private JdbcDAO jdbcDAO;

	public void testJdbc() {
		testCRUD();
	}

	private void testCRUD() {
		testCreate(jdbcDAO);
		testBatchUpdate(jdbcDAO);
		testQueryForSingleRow(jdbcDAO);
		testQueryFormultiRow(jdbcDAO);
		testDelete(jdbcDAO);
	}

	private void testCreate(JdbcDAO jdbcDAO) {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser2(paramForCreate1);
		jdbcDAO.createUser3(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate2);
		jdbcDAO.createUser5(paramForCreate1);
		jdbcDAO.createUser5(paramForCreate2);
	}

	private void testBatchUpdate(JdbcDAO jdbcDAO) {
		List<User> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDAO.batchUpdateUser1(users);
		jdbcDAO.batchUpdateUser2(users);
		jdbcDAO.batchUpdateUser3(users);
		jdbcDAO.batchUpdateUser4(paramForUpdate3, paramForUpdate4);
	}

	private void testQueryForSingleRow(JdbcDAO jdbcDAO) {
		jdbcDAO.queryForSingleColumn();
		jdbcDAO.queryForMultiColumn();
		jdbcDAO.queryForObject();
	}

	private void testQueryFormultiRow(JdbcDAO jdbcDAO) {
		jdbcDAO.queryForSingleColumnList();
		jdbcDAO.queryForMultiColumnList();
		jdbcDAO.queryForObjectList();
	}

	private void testDelete(JdbcDAO jdbcDAO) {
		jdbcDAO.deleteUser(2);
	}

	public JdbcDAO getJdbcDAO() {
		return jdbcDAO;
	}

	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}

}
