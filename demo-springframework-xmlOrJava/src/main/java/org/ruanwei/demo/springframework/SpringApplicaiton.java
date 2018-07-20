package org.ruanwei.demo.springframework;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.AppConfig;
import org.ruanwei.demo.springframework.core.aop.Good;
import org.ruanwei.demo.springframework.core.aop.Happy;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.event.MyApplicationEvent;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
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

	public static void main(String[] args) {
		test1(args);
	}

	private static void test1(String[] args) {

		log.info("0======================================================================================");
		// AbstractApplicationContext context =
		// getAnnotationConfigApplicationContext();
		AbstractApplicationContext context = getClassPathXmlApplicationContext();
		// AbstractApplicationContext context =
		// getFileSystemXmlApplicationContext();

		log.info("1======================================================================================");
		testEnvironment(context.getEnvironment());

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

	// GenericApplicationContext
	private static AbstractApplicationContext getAnnotationConfigApplicationContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				AppConfig.class);
		context.registerShutdownHook();

		// 要在加载bean定义之前进行上述设置并刷新
		// context.setConfigLocation("spring/applicationContext.xml");
		// context.register(AppConfig.class);
		// context.refresh();

		return context;
	}

	// AbstractRefreshableApplicationContext
	private static AbstractApplicationContext getClassPathXmlApplicationContext() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring/applicationContext.xml" });
		context.registerShutdownHook();
		return context;
	}

	// AbstractRefreshableApplicationContext
	private static AbstractApplicationContext getFileSystemXmlApplicationContext() {
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
				new String[] { "file:spring/applicationContext.xml" });
		context.registerShutdownHook();
		return context;
	}

	// StandardEnvironment/StandardServletEnvironment(spring-web)
	private static void testEnvironment(Environment env) {
		log.info("env==========" + env);

		testProfile(env);

		testPropertySource(env);
	}

	private static void testProfile(Environment env) {
		log.info("profiles==========" + env.getActiveProfiles() + " "
				+ env.getDefaultProfiles());

		if (env instanceof ConfigurableEnvironment) {
			ConfigurableEnvironment configEnv = (StandardEnvironment) env;
			// -Dspring.profiles.active="development"
			// -Dspring.profiles.default="production"
			configEnv.setActiveProfiles("development");
			configEnv.setDefaultProfiles("production");
		}
	}

	// StandardEnvironment:MapPropertySource(systemProperties)/SystemEnvironmentPropertySource(systemEnvironment)
	private static void testPropertySource(Environment env) {
		String a = env.getProperty("guest.name"); // @Value才可以取到PropertySourcesPlaceholderConfigurer的值
		String b = env.getProperty("b"); // -Db=3
											// MapPropertySource(systemProperties)/SystemEnvironmentPropertySource(systemEnvironment)
		String c = env.getProperty("p.username");// ResourcePropertySource(@PeopertySource("peopertySource.properties"))
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
				.getResource("spring/applicationContext.xml");
		log.info("resource==========" + resource);
	}

	private static void testApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		log.info("applicationEventPublisher=========="
				+ applicationEventPublisher);
		applicationEventPublisher.publishEvent(new MyApplicationEvent(null,
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

		Good good = (Good) context.getBean("good");
		Happy mixin = (Happy) context.getBean("good");
		log.info(good.good("whatever") + mixin.happy("whatever"));
	}

	private static void testApplicationEvent(ApplicationContext context) {
		log.info("context==========" + context);
		if (context instanceof AbstractApplicationContext) {
			AbstractApplicationContext absContext = (AbstractApplicationContext) context;
			log.info("7.1======================================================================================");
			absContext.start();

			log.info("7.2======================================================================================");
			absContext.stop();

			// AbstractRefreshableApplicationContext
			log.info("7.3======================================================================================");
			absContext.refresh();

			log.info("7.4======================================================================================");
			absContext.close();
		}
	}
}
