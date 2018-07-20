package org.ruanwei.demo.springframework;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.AppConfig3;
import org.ruanwei.demo.springframework.core.aop.Good3;
import org.ruanwei.demo.springframework.core.aop.Happy3;
import org.ruanwei.demo.springframework.core.ioc.Family3;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean3;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class SpringApplicaiton3 {
	private static Log log = LogFactory.getLog(SpringApplicaiton3.class);

	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig3.class);

		log.info("0======================================================================================");
		// AnnotationConfigApplicationContext context = getContext();

		context.registerShutdownHook();

		log.info("1======================================================================================");
		Family3 family = context.getBean("family", Family3.class);
		Family3 familyx = context.getBean("familyx", Family3.class);
		MyFamilyFactoryBean3 myFamilyFactoryBean = (MyFamilyFactoryBean3) context.getBean("&familyx");
		log.info(familyx);
		log.info(myFamilyFactoryBean);

		log.info("2======================================================================================");
		family.sayHello("whatever");

		log.info("2======================================================================================");
		Good3 good = (Good3) context.getBean("good");
		Happy3 mixin = (Happy3) context.getBean("good");
		log.info(good.good("whatever") + mixin.happy("whatever"));

		if (context instanceof AbstractApplicationContext) {
			log.info("3======================================================================================");
			context.start();

			log.info("4======================================================================================");
			context.stop();

			log.info("5======================================================================================");
			// GenericApplicationContext does not support multiple refresh attempts
			// context.refresh();

			log.info("6======================================================================================");
			context.close();
		}
	}

	private static AnnotationConfigApplicationContext getContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// context.scan("org.ruanwei.demo.springframework");

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
		context.register(AppConfig3.class);
		context.refresh();

		return context;
	}
}
