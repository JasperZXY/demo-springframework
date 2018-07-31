package org.ruanwei.demo.springframework.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.SpringApplicaiton2;
import org.ruanwei.demo.springframework.core.aop.Good2;
import org.ruanwei.demo.springframework.core.aop.Happy2;
import org.ruanwei.demo.springframework.core.ioc.AbsHouse;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.event.MyApplicationEvent2;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean2;
import org.ruanwei.demo.springframework.dataAccess.jdbc.SpringJdbcService;
import org.ruanwei.demo.springframework.dataAccess.springdata.SpringDataService;
import org.ruanwei.demo.springframework.dataAccess.tx.SpringTransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("contextService")
public class ContextService {
	private static Log log = LogFactory.getLog(ContextService.class);
	
	private ApplicationContext context;

	public  void testApplicationContext() {
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

	// StandardEnvironment/StandardServletEnvironment(spring-web)
	private  void testEnvironment(ApplicationContext context) {
		log.info("env==========" + context.getEnvironment());

		testProfile(context);

		testPropertySource(context.getEnvironment());
	}

	private  void testProfile(ApplicationContext context) {
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
	private  void testPropertySource(Environment env) {
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

	private  void testMessageSource(MessageSource messageSource) {
		log.info("messageSource==========" + messageSource);
		String msg = messageSource.getMessage("my.messageSource",
				new Object[] { "ruanwei" }, "This is my message source.",
				Locale.US);
		log.info("message==========" + msg);
	}

	private  void testResourceLoader(ResourceLoader resourceLoader) {
		log.info("resourceLoader==========" + resourceLoader);
		Resource resource = resourceLoader
				.getResource("classpath:spring/applicationContext.xml");
		log.info("resource==========" + resource);
	}

	private  void testApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		log.info("applicationEventPublisher=========="
				+ applicationEventPublisher);
		applicationEventPublisher.publishEvent(new MyApplicationEvent2(
				SpringApplicaiton2.class,
				"custom ApplicationEvent from SpringApplication"));
		applicationEventPublisher.publishEvent(new String(
				"PayloadApplicationEvent<String> from SpringApplication"));
	}

	private  void testIoC(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		Family familyx = context.getBean("familyx", Family.class);
		MyFamilyFactoryBean2 myFamilyFactoryBean = (MyFamilyFactoryBean2) context
				.getBean("&familyx");
		log.info(family);
		log.info(familyx);
		log.info(myFamilyFactoryBean);
	}

	private  void testAOP(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		family.sayHello("whatever");

		Good2 good = (Good2) context.getBean("good");
		Happy2 mixin = (Happy2) context.getBean("good");
		log.info(good.good("whatever") + mixin.happy("whatever"));
	}

	private  void testApplicationEvent(ApplicationContext context) {
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
			// absContext.close();
		}
	}

	
	
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
}
