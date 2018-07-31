package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.ruanwei.demo.springframework.dataAccess.jdbc.User;

public class JdbcTransaction {
	private static Log log = LogFactory.getLog(JdbcTransaction.class);

	private static final User paramForCreate3 = new User("ruanwei_tmp3", 35,
			Date.valueOf("1983-07-06"));
	private static final User paramForCreate4 = new User("ruanwei_tmp4", 35,
			Date.valueOf("1983-07-06"));

	private JdbcDAO jdbcDAO;

	public void testJdbcTransaction() {
		jdbcDAO.createUser1(paramForCreate3);
		jdbcDAO.createUser1(paramForCreate4);
	}

	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}
}
