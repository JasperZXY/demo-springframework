package org.ruanwei.demo.springframework.dataAccess.springdata;

import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.User2;
import org.ruanwei.demo.springframework.dataAccess.springdata.jdbc.UserJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.stereotype.Service;

@Service("springDataService")
public class SpringDataService {
	private static Log log = LogFactory.getLog(SpringDataService.class);

	private static final User2 paramForCreate = new User2("ruanwei_tmp88", 35,
			Date.valueOf("1983-07-06"));

	@Autowired
	private UserJdbcRepository userJdbcRepository;
	//@Autowired
	private SimpleJdbcRepository<User2, Integer> simpleJdbcRepository;

	public void testSpringData() {
		testSpringDataJdbc();
		testSpringDataRedis();
	}

	public void testSpringDataJdbc() {
		Iterable<User2> users = userJdbcRepository.findAll();
		users.forEach(u -> log.info("user2=" + u));
		//Iterable<User2> users2 = simpleJdbcRepository.findAll();
		//users2.forEach(u -> log.info("user2=" + u));
		//userJdbcRepository.save(paramForCreate);
	}

	public void testSpringDataRedis() {
	}
}
