package org.ruanwei.demo.springframework;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.HibernateValidator;
import org.ruanwei.demo.springframework.core.aop.GoodImpl2;
import org.ruanwei.demo.springframework.core.aop.MyAspect2;
import org.ruanwei.demo.springframework.core.ioc.People2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatAnnotationFormatterFactory2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatter2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormatterRegistrar2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeoplePropertyEditor2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeoplePropertyEditorRegistrar2;
import org.ruanwei.demo.springframework.core.ioc.databinding.StringToPeopleConverter2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
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
 * <li>@Import(DataAccessConfig.class)等价于&lt;import resource="dataAccess.xml"/>.
 * 
 * <li>@ImportResource("applicationContext.xml")等价于&lt;bean
 * class="example.AppConfig"/>.
 * 
 * <li>@Profile("dev")(可用于@Component)等价于&lt;beans profile="dev">.
 * 
 * <li>@Lazy(可用于@Component)等价于&lt;beans default-lazy-init="true">或&lt;bean
 * lazy-init="true" />.
 * 
 * <li>@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)(可用于@Component)等价于&lt;bean
 * scope="singleton" />.
 * 
 * <li>@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=
 * ScopedProxyMode.TARGET_CLASS)(可用于@Component)等价于&lt;bean
 * scope="prototype">&lt;aop:scoped-proxy proxy-target-class="true"/>&lt;/bean>或
 * &lt;context:component-scan scoped-proxy="interfaces" />.
 * 
 * <li>@DependsOn({ "bean1", "bean2" })(可用于@Component)等价于&lt;bean
 * depends-on="bean1,bean2" />.
 * 
 * <li>@Primary等价于(可用于@Component)等价于&lt;bean primary="true"
 * autowire-candidate="true" />.
 * 
 * <li>@Qualifier("first")(可用于@Component)等价于&lt;bean>&lt;qualifier
 * value="first"/>&lt;/bean>.
 * 
 * <li>@Bean(initMethod = "init", destroyMethod = "destroy")等价于&lt;bean
 * init-method="init" destroy-method="destroy" />.
 * 
 * <li>@Bean(autowire = Autowire.BY_NAME)等价于&lt;bean autowire="byName" />.
 * 
 * <li>@PropertySource("propertySource.properties")等价于
 * ctx.getEnvironment().getPropertySources().addFirst(new
 * ResourcePropertySource("propertySource.properties")).
 * 
 * <li>@ComponentScan(basePackages = { "org.ruanwei.demo"
 * })等价于&lt;context:component-scan base-package="org.ruanwei.demo">.
 * 
 * <li>@EnableAsync. <li>@EnableScheduling. <li>@EnableTransactionManagement.
 * <li>@EnableAspectJAutoProxy等价于&lt;aop:aspectj-autoproxy />. <li>
 * 
 * @EnableWebMvc.
 * 
 * @author ruanwei
 *
 */
// @Profile("development")
// @Profile("production")
// @ImportResource({"classpath:spring/applicationContext.xml"})
@Import(DataAccessConfig2.class)
@EnableAspectJAutoProxy
@PropertySource("classpath:propertySource-${spring.profiles.active:development}.properties")
@PropertySource("classpath:family.properties")
@PropertySource("classpath:jdbc.properties")
@ComponentScan(basePackages = { "org.ruanwei.demo.springframework" })
@Configuration
public class AppConfig2 {
	private static Log log = LogFactory.getLog(AppConfig2.class);

	private final DataAccessConfig2 dataAccessConfig;

	@Value("${family.familyCount:4}")
	private int familyCount;

	// @Inject
	// @Resource
	@Autowired
	private Environment env;

	@Autowired
	public AppConfig2(DataAccessConfig2 dataAccessConfig) {
		log.info("AppConfig()======");
		this.dataAccessConfig = dataAccessConfig;

		/*
		 * if (familyCount == 0) { familyCount =
		 * Integer.valueOf(env.getProperty("family.familyCount", "2")); }
		 */
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
		// registerConvertors(conversionService);

		// 方式二：单个指定Formatter/AnnotationFormatterFactory String->T
		// registerFormatters(conversionService);

		// 方式三：分组指定converters和formatters
		registerFormatterRegistrars(conversionService);

		log.info("conversionService==========" + conversionService);
		return conversionService;
	}

	// A.2.2.PropertyEditor-based Conversion omitted
	// @Bean
	public static CustomEditorConfigurer customEditorConfigurer() {
		CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();

		// 方式四：单个指定PropertyEditor
		registerPropertyEditors(customEditorConfigurer);

		// 方式五：分组指定PropertyEditor
		registerPropertyEditorRegistrars(customEditorConfigurer);

		log.info("customEditorConfigurer==========" + customEditorConfigurer);
		return customEditorConfigurer;
	}

	private void registerConvertors(
			FormattingConversionServiceFactoryBean conversionService) {
		Set<Object> converters = new HashSet<Object>();
		converters.add(new StringToPeopleConverter2());
		conversionService.setConverters(converters);
	}

	private void registerFormatters(
			FormattingConversionServiceFactoryBean conversionService) {
		Set<Object> formatters = new HashSet<Object>();
		formatters.add(new PeopleFormatter2());
		formatters.add(new PeopleFormatAnnotationFormatterFactory2());
		conversionService.setFormatters(formatters);
	}

	private void registerFormatterRegistrars(
			FormattingConversionServiceFactoryBean conversionService) {
		Set<FormatterRegistrar> formatterRegistrars = new HashSet<FormatterRegistrar>();
		formatterRegistrars.add(new PeopleFormatterRegistrar2());
		JodaTimeFormatterRegistrar jodaTimeFormatterRegistrar = new JodaTimeFormatterRegistrar();
		DateTimeFormatterFactoryBean dateTimeFormatterFactoryBean = new DateTimeFormatterFactoryBean();
		dateTimeFormatterFactoryBean.setPattern("yyyy-MM-dd");
		jodaTimeFormatterRegistrar
				.setDateFormatter(dateTimeFormatterFactoryBean.getObject());
		formatterRegistrars.add(jodaTimeFormatterRegistrar);
		conversionService.setFormatterRegistrars(formatterRegistrars);
	}

	private static void registerPropertyEditors(
			CustomEditorConfigurer customEditorConfigurer) {
		Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>();
		customEditors.put(People2.class, PeoplePropertyEditor2.class);
		customEditorConfigurer.setCustomEditors(customEditors);
	}

	private static void registerPropertyEditorRegistrars(
			CustomEditorConfigurer customEditorConfigurer) {
		customEditorConfigurer
				.setPropertyEditorRegistrars(new PeoplePropertyEditorRegistrar2[] { new PeoplePropertyEditorRegistrar2() });
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
	// A.4.1.Bean lifecycle callbacks
	// A.4.2.Context lifecycle callbacks

	// A.5.Environment：Profile and PropertySource
	// A.5.1.PropertySource：将@PropertySource加入到PropertyPlaceholderConfigurer，并同时可以被@Value和Environment访问
	// 方式一，通过指定@PropertySource，使得@Value支持占位符
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setFileEncoding("UTF-8");
		propertySourcesPlaceholderConfigurer
				.setOrder(Ordered.HIGHEST_PRECEDENCE);
		log.info("propertySourcesPlaceholderConfigurer=========="
				+ propertySourcesPlaceholderConfigurer);
		return propertySourcesPlaceholderConfigurer;
	}

	// 方式二，通过指定location/properties，使得@Value支持占位符
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer2() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setLocations(
				new ClassPathResource("family.properties"),
				new ClassPathResource("jdbc.properties"));
		propertySourcesPlaceholderConfigurer.setFileEncoding("UTF-8");
		propertySourcesPlaceholderConfigurer
				.setOrder(Ordered.HIGHEST_PRECEDENCE);
		log.info("propertySourcesPlaceholderConfigurer2=========="
				+ propertySourcesPlaceholderConfigurer);
		return propertySourcesPlaceholderConfigurer;
	}

	// A.5.2.Profile：参考@Profile和@Component

	// A.6.Extension Points
	// A.6.1.Customizing beans using a BeanPostProcessor
	// A.6.2.Customizing configuration metadata with a BeanFactoryPostProcessor
	// A.6.3.Customizing instantiation logic with a FactoryBean

	// ==========B.AOP and Instrumentation==========
	@Bean("myAspect")
	public MyAspect2 myAspect() {
		return new MyAspect2();
	}

	@Bean("good")
	public GoodImpl2 good() {
		GoodImpl2 good = new GoodImpl2();
		return good;
	}

}
