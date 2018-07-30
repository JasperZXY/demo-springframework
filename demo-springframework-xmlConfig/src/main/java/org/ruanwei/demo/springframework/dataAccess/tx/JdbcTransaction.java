package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.ruanwei.demo.springframework.dataAccess.User;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;

public class JdbcTransaction {
	
	private static final User paramForCreate1 = new User("ruanwei_tmp1", 35,
			Date.valueOf("1983-07-06"));
	private static final User paramForCreate2 = new User("ruanwei_tmp2", 35,
			Date.valueOf("1983-07-06"));

	private JdbcDAO jdbcDAO;

	public void testTransaction() {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser1(paramForCreate2);
		int i = 1/0;
	}

	public JdbcDAO getJdbcDAO() {
		return jdbcDAO;
	}

	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}
}
