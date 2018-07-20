package org.ruanwei.demo.springframework;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.AppConfig2;
import org.ruanwei.demo.springframework.core.aop.Good2;
import org.ruanwei.demo.springframework.core.aop.Happy2;
import org.ruanwei.demo.springframework.core.ioc.Family2;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class SpringApplicaiton2 {
	private static Log log = LogFactory.getLog(SpringApplicaiton2.class);

	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new String[] { "spring/applicationContext2.xml" });
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig2.class);
		
		log.info("0======================================================================================");
		// AnnotationConfigApplicationContext context = getContext();

		context.registerShutdownHook();

		log.info("1======================================================================================");
		Family2 family = context.getBean("family", Family2.class);
		Family2 familyx = context.getBean("familyx", Family2.class);
		MyFamilyFactoryBean2 myFamilyFactoryBean = (MyFamilyFactoryBean2) context.getBean("&familyx");
		log.info(familyx);
		log.info(myFamilyFactoryBean);

		log.info("2======================================================================================");
		family.sayHello("whatever");

		log.info("2======================================================================================");
		Good2 good = (Good2) context.getBean("good");
		Happy2 mixin = (Happy2) context.getBean("good");
		log.info(good.good("whatever") + mixin.happy("whatever"));

		if (context instanceof AbstractApplicationContext) {
			log.info("3======================================================================================");
			context.start();

			log.info("4======================================================================================");
			context.stop();

			log.info("5======================================================================================");
			// GenericApplicationContext does not support multiple refresh attempts.
			// context.refresh();

			log.info("6======================================================================================");
			context.close();
		}
	}
	
	private static AnnotationConfigApplicationContext getContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

		// StandardEnvironment/StandardServletEnvironment(spring-web)
		Environment env = context.getEnvironment();
		log.info("env==========" + env);
		if (env instanceof StandardEnvironment) {
			AbstractEnvironment absEnv = (StandardEnvironment) env;
			// -Dspring.profiles.active="development"
			// -Dspring.profiles.default="production"
			absEnv.setActiveProfiles("development");
			absEnv.setDefaultProfiles("production");

			// MapPropertySource(systemProperties)
			// SystemEnvironmentPropertySource(systemEnvironment)
			// ResourcePropertySource(@PropertySource(""))
			MutablePropertySources mutablePropertySources = absEnv.getPropertySources();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", 8);
			PropertySource<Map<String, Object>> mapPropertySource = new MapPropertySource("mapPropertySource", map);
			
			// SimpleCommandLinePropertySource(args)/ResourcePropertySource("classpath:*.properties");
			mutablePropertySources.addLast(mapPropertySource);
			for (PropertySource<?> propertySource : mutablePropertySources) {
				log.info("name=" + propertySource.getName() + " source=" + propertySource.getSource());
			}
			log.info("PropertySources==========" + mutablePropertySources);
		}

		// 要在加载bean定义之前进行上述设置并刷新
		// context.setConfigLocation("spring/applicationContext.xml");
		context.register(AppConfig2.class);
		context.refresh();

		return context;
	}
}
