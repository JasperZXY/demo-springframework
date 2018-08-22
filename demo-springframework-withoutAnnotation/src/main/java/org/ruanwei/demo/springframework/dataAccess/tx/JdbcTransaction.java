package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDao;
import org.ruanwei.demo.springframework.dataAccess.jdbc.User;

public class JdbcTransaction {
	private static Log log = LogFactory.getLog(JdbcTransaction.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp1", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate2 = new User("ruanwei_tmp2", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate3 = new User("ruanwei_tmp3", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate4 = new User("ruanwei_tmp4", 35, Date.valueOf("1983-07-06"));

	private JdbcDao jdbcDao;

	// 不能在这一层进行try-catch
	public void transactionalMethod() {
		jdbcDao.createUser1(paramForCreate1);
		jdbcDao.createUser1(paramForCreate2);

		transactionalSubMethod();

		int i = 1 / 0;
	}

	// 不能在这一层进行try-catch
	private void transactionalSubMethod() {
		jdbcDao.createUser1(paramForCreate3);
		jdbcDao.createUser1(paramForCreate4);
	}

	public void setJdbcDao(JdbcDao jdbcDao) {
		log.error("transactionalMethod+++++++"+jdbcDao);
		this.jdbcDao = jdbcDao;
	}
}
