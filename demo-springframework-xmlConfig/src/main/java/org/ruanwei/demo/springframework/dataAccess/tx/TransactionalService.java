package org.ruanwei.demo.springframework.dataAccess.tx;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalService {

	@Transactional
	public void testTransaction() {
	}
}
