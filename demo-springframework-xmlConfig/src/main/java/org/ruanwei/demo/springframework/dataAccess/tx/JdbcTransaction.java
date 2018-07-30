package org.ruanwei.demo.springframework.dataAccess.tx;

import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;

public class JdbcTransaction {
	
	private JdbcDAO jdbcDAO;

	public void testTransaction() {
	}

	public JdbcDAO getJdbcDAO() {
		return jdbcDAO;
	}

	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}
}
