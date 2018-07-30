package org.ruanwei.demo.springframework.dataAccess.jdbc;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.User2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcService {
	private static Log log = LogFactory.getLog(JdbcService.class);
	
	private static final User2 paramForCreate1 = new User2("ruanwei_tmp", 35,
			Date.valueOf("1983-07-06"));
	private static final User2 paramForUpdate1 = new User2("ruanwei", 18,
			Date.valueOf("1983-07-06"));
	private static final User2 paramForUpdate2 = new User2("ruanwei_tmp", 88,
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
	
	@Autowired
	private JdbcDAO2 jdbcDAO;

	public  void testJdbc() {
		testCRUD();
	}

	private  void testCRUD() {
		testCreate(jdbcDAO);
		testBatchUpdate(jdbcDAO);
		testQueryForSingleRow(jdbcDAO);
		testQueryFormultiRow(jdbcDAO);
		testDelete(jdbcDAO);
	}

	private  void testCreate(JdbcDAO2 jdbcDAO) {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser2(paramForCreate1);
		jdbcDAO.createUser3(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate2);
		jdbcDAO.createUser5(paramForCreate1);
		jdbcDAO.createUser5(paramForCreate2);
	}

	private  void testBatchUpdate(JdbcDAO2 jdbcDAO) {
		List<User2> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDAO.batchUpdateUser1(users);
		jdbcDAO.batchUpdateUser2(users);
		jdbcDAO.batchUpdateUser3(users);
		jdbcDAO.batchUpdateUser4(paramForUpdate3, paramForUpdate4);
	}

	private  void testQueryForSingleRow(JdbcDAO2 jdbcDAO) {
		jdbcDAO.queryForSingleColumn();
		jdbcDAO.queryForMultiColumn();
		jdbcDAO.queryForObject();
	}

	private  void testQueryFormultiRow(JdbcDAO2 jdbcDAO) {
		jdbcDAO.queryForSingleColumnList();
		jdbcDAO.queryForMultiColumnList();
		jdbcDAO.queryForObjectList();
	}

	private  void testDelete(JdbcDAO2 jdbcDAO) {
		jdbcDAO.deleteUser(2);
	}

}
