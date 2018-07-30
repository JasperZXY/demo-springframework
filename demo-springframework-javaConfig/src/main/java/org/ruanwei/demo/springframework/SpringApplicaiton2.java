package org.ruanwei.demo.springframework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.CoreService;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcService;
import org.ruanwei.demo.springframework.dataAccess.tx.TransactionalService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringApplicaiton2 {
	private static Log log = LogFactory.getLog(SpringApplicaiton2.class);

	private static AbstractApplicationContext context;

	public static void main(String[] args) {
		log.info("0======================================================================================");

		initApplicationContext(ApplicationContextType.ANNOTATION_CONFIG);

		CoreService coreService = context.getBean("coreService",
				CoreService.class);
		coreService.testCoreContainer();

		JdbcService jdbcService = context.getBean("jdbcService",
				JdbcService.class);
		jdbcService.testJdbc();

		TransactionalService transactionalService = context.getBean(
				"transactionalService", TransactionalService.class);
		transactionalService.testTransaction();

	}

	private static void initApplicationContext(ApplicationContextType type) {
		switch (type) {
		case ANNOTATION_CONFIG: {// GenericApplicationContext
			context = new AnnotationConfigApplicationContext(AppConfig2.class);
			// 要在getBean之前进行设置并刷新
			// context.setConfigLocation("spring/applicationContext.xml");
			// context.register(AppConfig.class);
			// context.refresh();
			break;
		}
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
