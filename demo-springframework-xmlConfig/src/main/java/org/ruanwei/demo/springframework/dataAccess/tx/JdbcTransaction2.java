package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.User;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class JdbcTransaction2 {
	private static Log log = LogFactory.getLog(JdbcTransaction2.class);

	private static final User paramForCreate3 = new User("ruanwei_tmp3", 35,
			Date.valueOf("1983-07-06"));
	private static final User paramForCreate4 = new User("ruanwei_tmp4", 35,
			Date.valueOf("1983-07-06"));

	private JdbcDAO jdbcDAO;

	// 不能在这一层进行try-catch
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { ArithmeticException.class })
	public void testTransaction2() {
		jdbcDAO.createUser1(paramForCreate3);
		jdbcDAO.createUser1(paramForCreate4);
	}
	
	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}
}
