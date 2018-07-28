package org.ruanwei.demo.springframework;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.aop.Good;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.House;
import org.ruanwei.demo.springframework.core.ioc.event.MyApplicationEvent;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean;
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

public class SpringApplicaiton {
	private static Log log = LogFactory.getLog(SpringApplicaiton.class);
	private static AbstractApplicationContext context;

	public static void main(String[] args) {
		log.info("0======================================================================================");
		getApplicationContext(ApplicationContextType.CLASSPATH_XML);

		// testCoreContainer();
		
		testDataAccess();
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
		testApplicationEvent(context);
	}

	private static void testDataAccess() {
		JdbcDAO jdbcDAO = context.getBean("jdbcDAO", JdbcDAO.class);
		jdbcDAO.queryForSingleColumn();
		jdbcDAO.queryForMultiColumn();
		jdbcDAO.queryForObject();
		
		jdbcDAO.queryForSingleColumnList();
		jdbcDAO.queryForMultiColumnList();
		jdbcDAO.queryForObjectList();
		
//		jdbcDAO.queryForList();
//		jdbcDAO.query();
//		
//		jdbcDAO.queryForList2();
//		jdbcDAO.queryForMultiColumn();
//		jdbcDAO.query2();
//		
//		jdbcDAO.queryWithParam();
//		jdbcDAO.queryWithParam2();
//		jdbcDAO.queryForListWithParam();
//		jdbcDAO.queryForListWithParam2();
//		jdbcDAO.queryForListWithParam3();
//		
//		jdbcDAO.queryWithNamedParam();
//		jdbcDAO.queryWithNamedParam2();
	}

	private static void getApplicationContext(ApplicationContextType type) {
		switch (type) {
		case ANNOTATION_CONFIG: {// GenericApplicationContext
			context = new AnnotationConfigApplicationContext(AppConfig.class);
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

		House house = context.getBean("house", House.class);
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
		applicationEventPublisher.publishEvent(new MyApplicationEvent(
				SpringApplicaiton.class,
				"custom ApplicationEvent from SpringApplication"));
		applicationEventPublisher.publishEvent(new String(
				"PayloadApplicationEvent<String> from SpringApplication"));
	}

	private static void testIoC(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		Family familyx = context.getBean("familyx", Family.class);
		MyFamilyFactoryBean myFamilyFactoryBean = (MyFamilyFactoryBean) context
				.getBean("&familyx");
		log.info(family);
		log.info(familyx);
		log.info(myFamilyFactoryBean);

	}

	private static void testAOP(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		family.sayHello("whatever");

		// TODO:Java配置下还不能正常工作
		Good good = (Good) context.getBean("good");
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

	public enum ApplicationContextType {
		CLASSPATH_XML, FILESYSTEM_XML, ANNOTATION_CONFIG
	}
}
