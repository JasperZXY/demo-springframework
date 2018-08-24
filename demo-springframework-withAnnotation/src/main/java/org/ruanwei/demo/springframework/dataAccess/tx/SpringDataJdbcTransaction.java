package org.ruanwei.demo.springframework.dataAccess.tx;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.data.jdbc.UserJdbcCrudRepository;
import org.ruanwei.demo.springframework.data.jdbc.UserJdbcPagingAndSortingRepository;
import org.ruanwei.demo.springframework.data.jdbc.UserJdbcRepository;
import org.ruanwei.demo.springframework.dataAccess.User;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("springDataJdbcTransaction")
public class SpringDataJdbcTransaction {
	private static Log log = LogFactory.getLog(SpringDataJdbcTransaction.class);

	private static final User paramForCreate1 = new User("ruanwei_tmp1", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate2 = new User("ruanwei_tmp2", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate3 = new User("ruanwei_tmp3", 35, Date.valueOf("1983-07-06"));
	private static final User paramForCreate4 = new User("ruanwei_tmp4", 35, Date.valueOf("1983-07-06"));

	@Autowired
	private UserJdbcRepository userJdbcRepository;

	@Autowired
	private UserJdbcCrudRepository userJdbcCrudRepository;

	@Autowired
	private UserJdbcPagingAndSortingRepository userJdbcPagingAndSortingRepository;

	// 不能在这一层进行try-catch
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ArithmeticException.class })
	public void transactionalMethod() {
		userJdbcRepository.createUser(paramForCreate1.getName(), paramForCreate1.getAge(),
				paramForCreate1.getBirthday());
		userJdbcCrudRepository.save(paramForCreate2);

		transactionalSubMethod();

		int i = 1 / 0;
	}

	// 不能在这一层进行try-catch
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { ArithmeticException.class })
	private void transactionalSubMethod() {
		userJdbcRepository.createUser(paramForCreate3.getName(), paramForCreate3.getAge(),
				paramForCreate3.getBirthday());
		userJdbcCrudRepository.save(paramForCreate4);
	}
}
