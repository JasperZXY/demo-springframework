package org.ruanwei.demo.springframework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.HibernateValidator;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatterRegistrar2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * <li>@Import(DataAccessConfig.class)等价于
 * <import resource="dataAccess-${env}.xml" />
 * 
 * <li>@ImportResource("classpath:spring/applicationContext-${env}.xml")等价于
 * <bean class="com.example.AppConfig"/>
 * 
 * <li>@Profile("dev")等价于(可用于@Component) <beans profile="dev">
 * 
 * <li>@Lazy等价于(可用于@Component)
 * <beans default-lazy-init="true">或<bean lazy-init="true" />
 * 
 * <li>@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)等价于(可用于@Component)
 * <bean scope="singleton" />
 * 
 * <li>@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=ScopedProxyMode.TARGET_CLASS)等价于(可用于@Component)
 * <bean scope="prototype"><aop:scoped-proxy proxy-target-class="true"/></bean>或
 * <context:component-scan scoped-proxy="interfaces" />
 * 
 * <li>@DependsOn({ "bean1", "bean2" })等价于(可用于@Component)
 * <bean depends-on="bean1,bean2" />
 * 
 * <li>@Primary等价于(可用于@Component)
 * <bean primary="true" autowire-candidate="true" />
 * 
 * <li>@Qualifier("first")等价于(可用于@Component)
 * <bean><qualifier value="first"/></bean>
 * 
 * <li>@Bean(initMethod = "init", destroyMethod = "destroy")等价于
 * <bean init-method="init" destroy-method="destroy" />
 * 
 * <li>@Bean(autowire = Autowire.BY_NAME)等价于 <bean autowire="byName" />
 * 
 * <li>@PropertySource("classpath:propertySource-${spring.profiles.active}.properties")等价于
 * ctx.getEnvironment().getPropertySources().addFirst(new
 * ResourcePropertySource(classpath:propertySource.properties));
 * 
 * <li>@ComponentScan(basePackages = { "org.ruanwei.demo.springframework" })等价于
 * <context:component-scan base-package="org.ruanwei.demo.springframework">
 * 
 * <li>@EnableAsync
 * <li>@EnableScheduling
 * <li>@EnableTransactionManagement
 * <li>@EnableAspectJAutoProxy等价于<aop:aspectj-autoproxy />
 * <li>@EnableWebMvc
 * 
 * @author Administrator
 *
 */
@Configuration
@PropertySource("classpath:propertySource-${spring.profiles.active:development}.properties")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "org.ruanwei.demo.springframework.core" })
public class AppConfig2 {
	private static Log log = LogFactory.getLog(AppConfig2.class);

	@Value("${p.username:ruanwei_def}")
	private String username;

	@Value("${p.password:mypass_def}")
	private String password;

	@Autowired
	@Inject
	@Resource
	private Environment env;

	@Bean("conversionService")
	public FormattingConversionServiceFactoryBean conversionService() {
		FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();
		PeopleFormatterRegistrar2 formatterRegistrar = new PeopleFormatterRegistrar2();
		Set<FormatterRegistrar> formatterRegistrars = new HashSet<FormatterRegistrar>();
		formatterRegistrars.add(formatterRegistrar);
		conversionService.setFormatterRegistrars(formatterRegistrars);
		log.info("conversionService==========" + conversionService);
		return conversionService;
	}

	@Bean("validator")
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setProviderClass(HibernateValidator.class);
		validator.setValidationMessageSource(messageSource());
		log.info("validator==========" + validator);
		return validator;
	}

	// JSR-303:Bean Validation 1.0
	@Bean
	public BeanValidationPostProcessor beanValidationPostProcessor() {
		BeanValidationPostProcessor beanValidationPostProcessor = new BeanValidationPostProcessor();
		beanValidationPostProcessor.setValidator(validator());
		log.info("beanValidationPostProcessor==========" + beanValidationPostProcessor);
		return beanValidationPostProcessor;
	}

	// JSR-349:Bean Validation 1.1
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		methodValidationPostProcessor.setValidator(validator());
		methodValidationPostProcessor.setOrder(0);
		log.info("methodValidationPostProcessor==========" + methodValidationPostProcessor);
		return methodValidationPostProcessor;
	}

	@Bean("messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("exception");
		messageSource.setCacheSeconds(10);
		messageSource.setDefaultEncoding("utf-8");
		log.info("messageSource==========" + messageSource);
		return messageSource;
	}

	// 注意，由于生命周期的原因，返回BeanFactoryPostProcessor的@Bean方法一定要声明为static，否则无法处理@Autowired、@Valu、@PostConstruct等注解
	// Static @Bean methods will not be enhanced for scoping and AOP semantics.A
	// WARN-level log message will be issued
	// for any non-static @Bean methods having a return type assignable to
	// BeanFactoryPostProcessor
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setLocations(new ClassPathResource("family.properties"),
				new ClassPathResource("jdbc.properties"));
		propertySourcesPlaceholderConfigurer.setFileEncoding("UTF-8");
		propertySourcesPlaceholderConfigurer.setOrder(0);
		log.info("propertySourcesPlaceholderConfigurer==========" + propertySourcesPlaceholderConfigurer);
		return propertySourcesPlaceholderConfigurer;
	}

	@Profile("development")
	@Bean("dataSource")
	@Lazy
	public DataSource standaloneDataSource() {
		log.info("standaloneDataSource()======" + username + password);
		if (username == null || username.isEmpty()) {
			username = env.getProperty("p.username", "ruanwei_def");
		}
		if (password == null || password.isEmpty()) {
			password = env.getProperty("p.password", "mypass_def");
		}
		return null;
	}

	@Profile("production")
	@Bean("dataSource")
	@Lazy
	public DataSource jndiDataSource() {
		log.info("jndiDataSource()======" + username + password);
		if (username == null || username.isEmpty()) {
			username = env.getProperty("p.username", "ruanwei_def");
		}
		if (password == null || password.isEmpty()) {
			password = env.getProperty("p.password", "mypass_def");
		}
		return null;
	}

	@Bean({ "someList2", "someList3" })
	@Description("Provides a basic example of a bean")
	@Lazy
	public List<Integer> someList2() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		return list;
	}

	@Bean({ "someSet2", "someSet3" })
	@Lazy
	public Set<Integer> someSet2() {
		Set<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(2);
		return set;
	}

	@Bean({ "someProperties2", "someProperties3" })
	@Lazy
	public Properties someProperties2() {
		Properties props = new Properties();
		props.put("a", 1);
		props.put("b", 2);
		return props;
	}

	@Bean({ "someMap2", "someMap3" })
	@Lazy
	public Map<String, Integer> someMap2() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		map.put("b", 2);
		return map;
	}

	@Bean("someField1")
	public FieldRetrievingFactoryBean someField1() {
		FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
		fieldRetrievingFactoryBean.setStaticField("java.lang.Math.PI");
		return fieldRetrievingFactoryBean;
	}

	@Bean("someField2")
	public PropertyPathFactoryBean someField2() {
		PropertyPathFactoryBean propertyPathFactoryBean = new PropertyPathFactoryBean();
		propertyPathFactoryBean.setTargetBeanName("father");
		propertyPathFactoryBean.setPropertyPath("name");
		return propertyPathFactoryBean;
	}

	@Bean("someField3")
	public MethodInvokingFactoryBean someField3() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		try {
			methodInvokingFactoryBean.setTargetObject(sysProps().getObject());
		} catch (Exception e) {
			log.error("", e);
		}
		methodInvokingFactoryBean.setTargetMethod("getProperty");
		methodInvokingFactoryBean.setArguments("java.version");
		return methodInvokingFactoryBean;
	}

	@Bean("sysProps")
	public MethodInvokingFactoryBean sysProps() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setTargetClass(System.class);
		methodInvokingFactoryBean.setTargetMethod("getProperties");
		return methodInvokingFactoryBean;
	}

}
