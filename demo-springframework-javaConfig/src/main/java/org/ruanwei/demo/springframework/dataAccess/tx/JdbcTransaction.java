package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.ruanwei.demo.springframework.dataAccess.User2;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager="transactionManager")
@Service("jdbcTransaction")
public class JdbcTransaction {

	private static final User2 paramForCreate1 = new User2("ruanwei_tmp1", 35,
			Date.valueOf("1983-07-06"));
	private static final User2 paramForCreate2 = new User2("ruanwei_tmp2", 35,
			Date.valueOf("1983-07-06"));

	@Autowired
	private JdbcDAO jdbcDAO;

	@Transactional(rollbackFor={ArithmeticException.class})
	public void testTransaction() {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser1(paramForCreate2);
		int i = 1/0;
	}
}
