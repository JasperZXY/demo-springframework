package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.ruanwei.demo.springframework.dataAccess.jdbc.User;

public class SpringTransactionService {
	private static Log log = LogFactory.getLog(SpringTransactionService.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp1", 35,
			Date.valueOf("1983-07-06"));
	private static final User paramForCreate2 = new User("ruanwei_tmp2", 35,
			Date.valueOf("1983-07-06"));

	private JdbcTransaction jdbcTransaction;
	private JdbcDAO jdbcDAO;

	public void testSpringTransaction() {
		testJdbcTransaction();
		testJtaTransaction();
	}

	// 不能在这一层进行try-catch
	public void testJdbcTransaction() {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser1(paramForCreate2);

		jdbcTransaction.testJdbcTransaction();

		int i = 1 / 0;
	}

	public void testJtaTransaction() {

	}

	public void setJdbcTransaction(JdbcTransaction jdbcTransaction) {
		this.jdbcTransaction = jdbcTransaction;
	}

	public void setJdbcDAO(JdbcDAO jdbcDAO) {
		this.jdbcDAO = jdbcDAO;
	}
}
