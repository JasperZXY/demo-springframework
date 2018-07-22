package org.ruanwei.demo.springframework;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.HibernateValidator;
import org.ruanwei.demo.springframework.core.aop.GoodImpl;
import org.ruanwei.demo.springframework.core.aop.MyAspect;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.FamilyFactory;
import org.ruanwei.demo.springframework.core.ioc.House;
import org.ruanwei.demo.springframework.core.ioc.People;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatAnnotationFormatterFactory;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatter;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatterRegistrar;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeoplePropertyEditor;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeoplePropertyEditorRegistrar;
import org.ruanwei.demo.springframework.core.ioc.databinding.StringToPeopleConverter;
import org.ruanwei.demo.springframework.core.ioc.extension.MyBeanFactoryPostProcessor;
import org.ruanwei.demo.springframework.core.ioc.extension.MyBeanPostProcessor;
import org.ruanwei.demo.springframework.core.ioc.extension.MyFamilyFactoryBean;
import org.ruanwei.demo.springframework.core.ioc.lifecycle.MyDisposableBean;
import org.ruanwei.demo.springframework.core.ioc.lifecycle.MyInitializingBean;
import org.ruanwei.demo.springframework.core.ioc.lifecycle.MyLifecycle;
import org.ruanwei.demo.springframework.core.ioc.lifecycle.MyLifecycleProcessor;
import org.ruanwei.demo.springframework.core.ioc.lifecycle.MySmartLifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * <li>@Import(DataAccessConfig.class)等价于 <import
 * resource="dataAccess-${env}.xml" />
 * 
 * <li>@ImportResource("classpath:spring/applicationContext-${env}.xml")等价于
 * <bean class="com.example.AppConfig"/>
 * 
 * <li>@Profile("dev")等价于(可用于@Component) <beans profile="dev">
 * 
 * <li>@Lazy等价于(可用于@Component) <beans default-lazy-init="true">或<bean
 * lazy-init="true" />
 * 
 * <li>@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)等价于(可用于@Component) <bean
 * scope="singleton" />
 * 
 * <li>@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=
 * ScopedProxyMode.TARGET_CLASS)等价于(可用于@Component) <bean
 * scope="prototype"><aop:scoped-proxy proxy-target-class="true"/></bean>或
 * <context:component-scan scoped-proxy="interfaces" />
 * 
 * <li>@DependsOn({ "bean1", "bean2" })等价于(可用于@Component) <bean
 * depends-on="bean1,bean2" />
 * 
 * <li>@Primary等价于(可用于@Component) <bean primary="true" autowire-candidate="true"
 * />
 * 
 * <li>@Qualifier("first")等价于(可用于@Component) <bean><qualifier
 * value="first"/></bean>
 * 
 * <li>@Bean(initMethod = "init", destroyMethod = "destroy")等价于 <bean
 * init-method="init" destroy-method="destroy" />
 * 
 * <li>@Bean(autowire = Autowire.BY_NAME)等价于 <bean autowire="byName" />
 * 
 * <li>@PropertySource(
 * "classpath:propertySource-${spring.profiles.active}.properties")等价于
 * ctx.getEnvironment().getPropertySources().addFirst(new
 * ResourcePropertySource(classpath:propertySource.properties));
 * 
 * <li>@ComponentScan(basePackages = { "org.ruanwei.demo.springframework" })等价于
 * <context:component-scan base-package="org.ruanwei.demo.springframework">
 * 
 * <li>@EnableAsync <li>@EnableScheduling <li>@EnableTransactionManagement <li>
 * 
 * @EnableAspectJAutoProxy等价于<aop:aspectj-autoproxy /> <li>@EnableWebMvc
 * 
 * @author Administrator
 *
 */
@Import(DataConfig.class)
@EnableAspectJAutoProxy
@PropertySource("classpath:propertySource-${spring.profiles.active:development}.properties")
@PropertySource("classpath:family.properties")
@Configuration
public class AppConfig {
	private static Log log = LogFactory.getLog(AppConfig.class);

	@Autowired
	private DataConfig dataConfig;

	// TODO:@Value在这里取不到值，但在@Bean方法中可以,why
	@Value("${family.familyCount:4}")
	private int familyCount;
	@Value("#{father}")
	private People father;
	@Value("#{mother}")
	private People mother;
	@Value("${son.all}")
	private People son;
	@Value("${daughter.all}")
	private People daughter;
	@Value("#{guest1}")
	private People guest1;

	@Value("1,2")
	private Integer[] someArray;
	@Value("3,4")
	private List<Integer> someList;
	@Value("5,6")
	private Set<Integer> someSet;
	@Value("a=1,b=2")
	private Properties someProperties;
	// @Value("c=3,d=4")
	private Map<String, Integer> someMap;

	@Value("${p.username:ruanwei_def}")
	private String username;

	@Value("${p.password:mypass_def}")
	private String password;

	@Autowired
	@Inject
	@Resource
	private Environment env;

	public AppConfig() {
		log.info("AppConfig()======" + username + password);

		// if (username == null || username.isEmpty()) {
		// username = env.getProperty("p.username", "ruanwei_def");
		// }
		// if (password == null || password.isEmpty()) {
		// password = env.getProperty("p.password", "mypass_def");
		// }
	}

	// ==========A.The IoC Container==========
	// A.1.Bean Definition and Dependency Injection
	// A.1.1.Bean instantiation with a constructor
	@Lazy
	@DependsOn("house")
	@Bean(name = "family", initMethod = "init", destroyMethod = "destroy")
	public Family family(
			@Value("${family.1.familyName:ruan_def}") String familyName,
			@Value("#{father}") People father) {
		// 1.Constructor-based dependency injection
		Family family = new Family(familyName, familyCount, father);
		// 2.Setter-based dependency injection
		family.setMother(mother);
		family.setSon(son);
		family.setDaughter(daughter);
		// Proxied scoped beans as dependencies
		family.setGuest1(guest1);
		return family;
	}

	// A.1.2.Bean instantiation with a static factory method
	@Lazy
	@DependsOn("house")
	@Bean("family2")
	public Family family2(
			@Value("${family.2.familyName:ruan_def2}") String familyName) {
		Family family = FamilyFactory.createInstance1(familyName, familyCount,
				father);
		family.setMother(mother);
		family.setSon(son);
		family.setDaughter(daughter);
		family.setGuest1(guest1);
		return family;
	}

	// A.1.3.Bean instantiation using an instance factory method
	@Lazy
	@DependsOn("house")
	@Bean("family3")
	public Family family3(
			@Value("${family.3.familyName:ruan_def3}") String familyName) {
		Family family = familyFactory().createInstance2(familyName,
				familyCount, father);
		family.setMother(mother);
		family.setSon(son);
		family.setDaughter(daughter);
		family.setGuest1(guest1);
		return family;
	}

	@Lazy
	@Bean("familyFactory")
	public FamilyFactory familyFactory() {
		return new FamilyFactory();
	}

	@Lazy
	@Bean("father")
	public People father(@Value("${father.name:ruanwei_def}") String name,
			@Value("${father.age:35}") int age) {
		return new People(name, age);
	}

	// @Lazy
	@Bean("mother")
	public People mother(@Value("${mother.name:lixiaoling_def}") String name,
			@Value("${mother.age:34}") int age) {
		return new People(name, age);
	}

	@Lazy
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	@Bean("guest1")
	public People guest1(@Value("${guest.name:ruan_def}") String name,
			@Value("${guest.age:100}") int age) {
		return new People(name, age);
	}

	@Bean("absHouse")
	public House absHouse(
			@Value("${abshouse.name:houseName_def}") String houseName,
			@Value("#{someField1}") double someField1,
			@Value("#{someField2}") String someField2,
			@Value("#{someField3}") String someField3) {
		House abshouse = new House();

		abshouse.setHouseName(houseName);

		abshouse.setSomeArray(someArray);
		abshouse.setSomeList(someList);
		abshouse.setSomeSet(someSet);
		abshouse.setSomeProperties(someProperties);
		abshouse.setSomeMap(someMap);

		abshouse.setSomeList2(someList2());
		abshouse.setSomeSet2(someSet2());
		abshouse.setSomeProperties2(someProperties2());
		abshouse.setSomeMap2(someMap2());

		abshouse.setSomeField1(someField1);
		abshouse.setSomeField2(someField2);
		abshouse.setSomeField3(someField3);

		return abshouse;
	}

	@Lazy
	@Description("result in a List bean,see ListFactoryBean")
	@Bean({ "someList2", "someList3" })
	public List<Integer> someList2() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		return list;
	}

	@Lazy
	@Description("result in a Set bean,see SetFactoryBean")
	@Bean({ "someSet2", "someSet3" })
	public Set<Integer> someSet2() {
		Set<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(2);
		return set;
	}

	@Lazy
	@Description("result in a Properties bean,see PropertiesFactoryBean")
	@Bean({ "someProperties2", "someProperties3" })
	public Properties someProperties2() {
		Properties props = new Properties();
		props.put("a", 1);
		props.put("b", 2);
		return props;
	}

	@Lazy
	@Description("result in a Map bean,see MapFactoryBean")
	@Bean({ "someMap2", "someMap3" })
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

	// A.2.Data Binding
	// A.2.1.ConversionService-based Type Conversion and Formatting
	@Bean("conversionService")
	public FormattingConversionServiceFactoryBean conversionService() {
		FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();
		conversionService.setRegisterDefaultFormatters(true);

		// 方式一：单个指定Converter/ConverterFactory/GenericConverter S->T
		Set<Object> converters = new HashSet<Object>();
		converters.add(new StringToPeopleConverter());
		conversionService.setConverters(converters);

		// 方式二：单个指定Formatter/AnnotationFormatterFactory String->T
		Set<Object> formatters = new HashSet<Object>();
		formatters.add(new PeopleFormatter());
		formatters.add(new PeopleFormatAnnotationFormatterFactory());
		conversionService.setFormatters(formatters);

		// 方式三：分组指定converters和formatters
		Set<FormatterRegistrar> formatterRegistrars = new HashSet<FormatterRegistrar>();
		formatterRegistrars.add(new PeopleFormatterRegistrar());
		JodaTimeFormatterRegistrar jodaTimeFormatterRegistrar = new JodaTimeFormatterRegistrar();
		DateTimeFormatterFactoryBean dateTimeFormatterFactoryBean = new DateTimeFormatterFactoryBean();
		dateTimeFormatterFactoryBean.setPattern("yyyy-MM-dd");
		jodaTimeFormatterRegistrar
				.setDateFormatter(dateTimeFormatterFactoryBean.getObject());
		formatterRegistrars.add(jodaTimeFormatterRegistrar);
		conversionService.setFormatterRegistrars(formatterRegistrars);

		log.info("conversionService==========" + conversionService);
		return conversionService;
	}

	// A.2.2.PropertyEditor-based Conversion
	@Bean
	public static CustomEditorConfigurer customEditorConfigurer() {
		CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();

		// 方式四：单个指定PropertyEditor
		Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>();
		customEditors.put(People.class, PeoplePropertyEditor.class);
		customEditorConfigurer.setCustomEditors(customEditors);

		// 方式五：分组指定PropertyEditor
		customEditorConfigurer
				.setPropertyEditorRegistrars(new PeoplePropertyEditorRegistrar[] { new PeoplePropertyEditorRegistrar() });

		log.info("customEditorConfigurer==========" + customEditorConfigurer);
		return customEditorConfigurer;
	}

	// A.2.3.Validation JSR-303/JSR-349/JSR-380
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
		log.info("beanValidationPostProcessor=========="
				+ beanValidationPostProcessor);
		return beanValidationPostProcessor;
	}

	// JSR-349:Bean Validation 1.1
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		methodValidationPostProcessor.setValidator(validator());
		methodValidationPostProcessor.setOrder(0);
		log.info("methodValidationPostProcessor=========="
				+ methodValidationPostProcessor);
		return methodValidationPostProcessor;
	}

	// A.3.Internationalization:MessageSource/ResourceBundleMessageSource
	@Bean("messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("exception");
		messageSource.setCacheSeconds(10);
		messageSource.setDefaultEncoding("utf-8");
		log.info("messageSource==========" + messageSource);
		return messageSource;
	}

	// A.4.Lifecycle:Initialization/Destruction/Startup/Shutdown callbacks
	// A.4.1.Bean lifecycle callbacks -->
	@Bean
	public MyInitializingBean myInitializingBean() {
		return new MyInitializingBean();
	}

	@Bean
	public MyDisposableBean myDisposableBean() {
		return new MyDisposableBean();
	}

	// A.4.2.Context lifecycle callbacks
	@Bean
	public MyLifecycle myLifecycle() {
		return new MyLifecycle();
	}

	@Bean
	public MySmartLifecycle mySmartLifecycle() {
		return new MySmartLifecycle();
	}

	@Bean
	public MyLifecycleProcessor myLifecycleProcessor() {
		return new MyLifecycleProcessor();
	}

	// A.5.Environment：Profile and PropertySource

	// A.5.1.PropertySource：将@PropertySource加入到PropertyPlaceholderConfigurer，并同时可以被@Value和Environment访问
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setLocations(
				new ClassPathResource("family.properties"),
				new ClassPathResource("jdbc.properties"));
		propertySourcesPlaceholderConfigurer.setFileEncoding("UTF-8");
		propertySourcesPlaceholderConfigurer
				.setOrder(Ordered.HIGHEST_PRECEDENCE);
		log.info("propertySourcesPlaceholderConfigurer=========="
				+ propertySourcesPlaceholderConfigurer);
		return propertySourcesPlaceholderConfigurer;
	}

	// A.5.2.Profile：必须放在配置文件最后
	@Profile("development")
	@Bean("house")
	public House house1(
			@Value("${house.name:houseName_def}") String houseName,
			@Value("${house.host.development:development_def}") String hostName,
			@Value("#{absHouse}") House abshouse) {
		abshouse.setHouseName(houseName);
		abshouse.setHostName(hostName);
		return abshouse;
	}

	@Profile("production")
	@Bean("house")
	public House house2(@Value("${house.name:houseName_def}") String houseName,
			@Value("${house.host.production:production_def}") String hostName,
			@Value("#{absHouse}") House abshouse) {
		abshouse.setHouseName(houseName);
		abshouse.setHostName(hostName);
		return abshouse;
	}

	// A.6.Extension Points
	// A.6.1.Customizing beans using a BeanPostProcessor
	@Order(1)
	@Bean
	public MyBeanPostProcessor myBeanPostProcessor() {
		MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
		myBeanPostProcessor.setValidator(validator());
		myBeanPostProcessor.setValidatorFactory(validator());
		myBeanPostProcessor.setSpringValidator(validator());
		return myBeanPostProcessor;
	}

	// A.6.2.Customizing configuration metadata with a BeanFactoryPostProcessor
	// 注意，由于生命周期的原因，返回BeanFactoryPostProcessor的@Bean方法一定要声明为static，否则无法处理@Autowired、@Value、@PostConstruct等注解
	// Static @Bean methods will not be enhanced for scoping and AOP semantics.A
	// WARN-level log message will be issued
	// for any non-static @Bean methods having a return type assignable to
	// BeanFactoryPostProcessor
	@Order(1)
	@Bean
	public static MyBeanFactoryPostProcessor myBeanFactoryPostProcessor() {
		return new MyBeanFactoryPostProcessor();
	}

	// A.6.3.Customizing instantiation logic with a FactoryBean
	@Bean("familyx")
	public MyFamilyFactoryBean myFamilyFactoryBean(
			@Value("${family.x.familyName:ruan_def}") String familyName) {
		MyFamilyFactoryBean myFamilyFactoryBean = new MyFamilyFactoryBean(
				familyName, familyCount, father);
		return myFamilyFactoryBean;
	}

	// ==========B.AOP and Instrumentation==========
	@Bean("myAspect")
	public MyAspect myAspect() {
		return new MyAspect();
	}

	@Bean("good")
	public GoodImpl good() {
		GoodImpl good = new GoodImpl();
		return good;
	}

}
