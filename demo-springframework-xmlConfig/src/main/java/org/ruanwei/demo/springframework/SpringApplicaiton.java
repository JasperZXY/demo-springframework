package org.ruanwei.demo.springframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ContextService;
import org.ruanwei.demo.springframework.dataAccess.jdbc.SpringJdbcService;
import org.ruanwei.demo.springframework.dataAccess.springdata.SpringDataService;
import org.ruanwei.demo.springframework.dataAccess.tx.SpringTransactionService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringApplicaiton {
	private static Log log = LogFactory.getLog(SpringApplicaiton.class);

	private static AbstractApplicationContext context;
	static {
		log.info("0======================================================================================");
		initApplicationContext(ApplicationContextType.CLASSPATH_XML);
		log.info("0======================================================================================");
	}

	public static void main(String[] args) {
		testApplicationContext();
		testDataAccess();
	}

	private static void testApplicationContext() {
		ContextService contextService = context.getBean("contextService",
				ContextService.class);
		contextService.setContext(context);
		contextService.testApplicationContext();
	}

	private static void testDataAccess() {
		testSpringJdbcService();
		testJdbcTransactionService();
		testSpringDataService();
	}

	private static void testSpringJdbcService() {
		SpringJdbcService springJdbcService = context.getBean(
				"springJdbcService", SpringJdbcService.class);
		springJdbcService.testSpringJdbc();
	}

	private static void testJdbcTransactionService() {
		SpringTransactionService springTransactionService = context.getBean(
				"springTransactionService", SpringTransactionService.class);
		try {
			springTransactionService.testJdbcTransaction();
		} catch (Exception e) {
			log.error("transaction rolled back", e);
		}
	}

	private static void testSpringDataService() {
		SpringDataService springDataService = context.getBean(
				"springDataService", SpringDataService.class);
		springDataService.testSpringData();
	}

	private static void initApplicationContext(ApplicationContextType type) {
		switch (type) {
		case CLASSPATH_XML: {// AbstractRefreshableApplicationContext
			context = new ClassPathXmlApplicationContext(
					new String[] { "classpath:spring/applicationContext.xml" });
			break;
		}
		case FILESYSTEM_XML: {// AbstractRefreshableApplicationContext
			context = new FileSystemXmlApplicationContext(
					new String[] { "file:spring/applicationContext.xml" });
			break;
		}
		default:
		}

		context.registerShutdownHook();
	}

	public enum ApplicationContextType {
		CLASSPATH_XML, FILESYSTEM_XML, ANNOTATION_CONFIG
	}
}
