package org.ruanwei.demo.springframework;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.aop.Good2;
import org.ruanwei.demo.springframework.core.ioc.AbsHouse;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.event.MyApplicationEvent2;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean2;
import org.ruanwei.demo.springframework.dataAccess.User2;
import org.ruanwei.demo.springframework.dataAccess.jdbc.JdbcDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class SpringApplicaiton2 {
	private static Log log = LogFactory.getLog(SpringApplicaiton2.class);

	private static final User2 paramForCreate1 = new User2("ruanwei_tmp", 35,
			Date.valueOf("1983-07-06"));
	private static final User2 paramForUpdate1 = new User2("ruanwei", 18,
			Date.valueOf("1983-07-06"));
	private static final User2 paramForUpdate2 = new User2("ruanwei_tmp", 88,
			Date.valueOf("1983-07-06"));

	private static final Map<String, Object> paramForCreate2 = new HashMap<String, Object>();
	private static final Map<String, Object> paramForUpdate3 = new HashMap<String, Object>();
	private static final Map<String, Object> paramForUpdate4 = new HashMap<String, Object>();

	private static AbstractApplicationContext context;

	static {
		paramForCreate2.put("name", "ruanwei_tmp");
		paramForCreate2.put("age", 35);
		paramForCreate2.put("birthday", Date.valueOf("1983-07-06"));

		paramForUpdate3.put("name", "ruanwei");
		paramForUpdate3.put("age", 18);
		paramForUpdate3.put("birthday", Date.valueOf("1983-07-06"));

		paramForUpdate4.put("name", "ruanwei_tmp");
		paramForUpdate4.put("age", 88);
		paramForUpdate4.put("birthday", Date.valueOf("1983-07-06"));

		log.info("0======================================================================================");
		initApplicationContext(ApplicationContextType.ANNOTATION_CONFIG);
		log.info("0======================================================================================");
	}

	public static void main(String[] args) {
		testCoreContainer();
		testJdbc();
		testTransaction();
	}

	private static void testCoreContainer() {
		log.info("1======================================================================================");
		testEnvironment(context);

		log.info("2======================================================================================");
		testMessageSource(context);

		log.info("3======================================================================================");
		testResourceLoader(context);

		log.info("4======================================================================================");
		testApplicationEventPublisher(context);

		log.info("5======================================================================================");
		testIoC(context);

		log.info("6======================================================================================");
		testAOP(context);

		log.info("7======================================================================================");
		// testApplicationEvent(context);
	}

	// StandardEnvironment/StandardServletEnvironment(spring-web)
	private static void testEnvironment(ApplicationContext context) {
		log.info("env==========" + context.getEnvironment());

		testProfile(context);

		testPropertySource(context.getEnvironment());
	}

	private static void testProfile(ApplicationContext context) {
		Environment env = context.getEnvironment();
		log.info("profiles==========" + env.getActiveProfiles() + " "
				+ env.getDefaultProfiles());

		if (env instanceof ConfigurableEnvironment) {
			ConfigurableEnvironment configEnv = (StandardEnvironment) env;
			// -Dspring.profiles.active="production"
			// -Dspring.profiles.default="development"
			configEnv.setActiveProfiles("development");
			configEnv.setDefaultProfiles("production");
		}

		AbsHouse house = context.getBean("house", AbsHouse.class);
		log.info("house==========" + house);
	}

	// StandardEnvironment:MapPropertySource(systemProperties)/SystemEnvironmentPropertySource(systemEnvironment)
	private static void testPropertySource(Environment env) {
		// TODO:ClassPathXmlApplicationContext不能获取到属性值，但是annotation那个项目却可以
		String a = env.getProperty("a", "a"); // MapPropertySource(-Da=1)
		String b = env.getProperty("family.familyCount", "2");// ResourcePropertySource(@PeopertySource("family.properties"))
		String c = env.getProperty("guest.name"); // PropertySourcesPlaceholderConfigurer支持PropertySource参与占位符替换
		log.info("property=========a=" + a + " b=" + b + " c=" + c);

		if (env instanceof ConfigurableEnvironment) {
			ConfigurableEnvironment configEnv = (ConfigurableEnvironment) env;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", 8);
			PropertySource<Map<String, Object>> mapPropertySource = new MapPropertySource(
					"mapPropertySource", map);

			// EnumerablePropertySource:CommandLinePropertySource/MapPropertySource/ServletConfigPropertySource/ServletContextPropertySource
			// CommandLinePropertySource:SimpleCommandLinePropertySource/JOptCommandLinePropertySource
			// MapPropertySource:PropertiesPropertySource/SystemEnvironmentPropertySource
			// PropertiesPropertySource:ResourcePropertySource("classpath:*.properties")/MockPropertySource
			MutablePropertySources propertySources = configEnv
					.getPropertySources();
			propertySources.addLast(mapPropertySource);
			log.info("PropertySources==========" + propertySources);
		}

	}

	private static void testMessageSource(MessageSource messageSource) {
		log.info("messageSource==========" + messageSource);
		String msg = messageSource.getMessage("my.messageSource",
				new Object[] { "ruanwei" }, "This is my message source.",
				Locale.US);
		log.info("message==========" + msg);
	}

	private static void testResourceLoader(ResourceLoader resourceLoader) {
		log.info("resourceLoader==========" + resourceLoader);
		Resource resource = resourceLoader
				.getResource("classpath:spring/applicationContext.xml");
		log.info("resource==========" + resource);
	}

	private static void testApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		log.info("applicationEventPublisher=========="
				+ applicationEventPublisher);
		applicationEventPublisher.publishEvent(new MyApplicationEvent2(
				SpringApplicaiton2.class,
				"custom ApplicationEvent from SpringApplication"));
		applicationEventPublisher.publishEvent(new String(
				"PayloadApplicationEvent<String> from SpringApplication"));
	}

	private static void testIoC(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		Family familyx = context.getBean("familyx", Family.class);
		MyFamilyFactoryBean2 myFamilyFactoryBean = (MyFamilyFactoryBean2) context
				.getBean("&familyx");
		log.info(family);
		log.info(familyx);
		log.info(myFamilyFactoryBean);

	}

	private static void testAOP(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		family.sayHello("whatever");

		// TODO:Java配置下还不能正常工作
		Good2 good = (Good2) context.getBean("good");
		// Happy mixin = (Happy) context.getBean("good");
		// log.info(good.good("whatever") + mixin.happy("whatever"));
	}

	private static void testApplicationEvent(ApplicationContext context) {
		log.info("context==========" + context);
		if (context instanceof AbstractApplicationContext) {
			AbstractApplicationContext absContext = (AbstractApplicationContext) context;
			log.info("7.1======================================================================================");
			absContext.start();

			log.info("7.2======================================================================================");
			absContext.stop();

			log.info("7.3======================================================================================");
			// 即ClassPathXmlApplicationContext和FileSystemXmlApplicationContext
			if (context instanceof AbstractRefreshableApplicationContext) {
				absContext.refresh();
			}

			log.info("7.4======================================================================================");
			absContext.close();
		}
	}

	private static void testJdbc() {
		JdbcDAO jdbcDAO = context.getBean("jdbcDAO", JdbcDAO.class);

		testCRUD(jdbcDAO);
	}

	private static void testCRUD(JdbcDAO jdbcDAO) {
		testCreate(jdbcDAO);
		testBatchUpdate(jdbcDAO);
		testQueryForSingleRow(jdbcDAO);
		testQueryFormultiRow(jdbcDAO);
		testDelete(jdbcDAO);
	}

	private static void testCreate(JdbcDAO jdbcDAO) {
		jdbcDAO.createUser1(paramForCreate1);
		jdbcDAO.createUser2(paramForCreate1);
		jdbcDAO.createUser3(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate1);
		jdbcDAO.createUser4(paramForCreate2);
		jdbcDAO.createUser5(paramForCreate1);
		jdbcDAO.createUser5(paramForCreate2);
	}

	private static void testBatchUpdate(JdbcDAO jdbcDAO) {
		List<User2> users = Arrays.asList(paramForUpdate1, paramForUpdate2);
		jdbcDAO.batchUpdateUser1(users);
		jdbcDAO.batchUpdateUser2(users);
		jdbcDAO.batchUpdateUser3(users);
		jdbcDAO.batchUpdateUser4(paramForUpdate3, paramForUpdate4);
	}

	private static void testQueryForSingleRow(JdbcDAO jdbcDAO) {
		jdbcDAO.queryForSingleColumn();
		jdbcDAO.queryForMultiColumn();
		jdbcDAO.queryForObject();
	}

	private static void testQueryFormultiRow(JdbcDAO jdbcDAO) {
		jdbcDAO.queryForSingleColumnList();
		jdbcDAO.queryForMultiColumnList();
		jdbcDAO.queryForObjectList();
	}

	private static void testDelete(JdbcDAO jdbcDAO) {
		jdbcDAO.deleteUser(2);
	}

	private static void testTransaction() {
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
					new String[] { "classpath:spring/applicationContext2.xml" });
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
