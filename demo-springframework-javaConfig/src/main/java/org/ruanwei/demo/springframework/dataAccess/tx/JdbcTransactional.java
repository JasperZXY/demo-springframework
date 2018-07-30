package org.ruanwei.demo.springframework.dataAccess.tx;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JdbcTransactional {

	@Transactional
	public void testTransaction() {
	}
}
