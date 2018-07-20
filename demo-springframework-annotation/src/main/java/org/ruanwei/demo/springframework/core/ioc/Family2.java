package org.ruanwei.demo.springframework.core.ioc;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormat2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormat2.Separator;
import org.ruanwei.demo.springframework.core.ioc.event.MyApplicationEvent2;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.stereotype.Component;

@Lazy
@DependsOn("house")
@Component("family")
public class Family2 implements BeanNameAware, BeanClassLoaderAware, LoadTimeWeaverAware {
	private static Log log = LogFactory.getLog(Family2.class);

	private String familyName;
	private int familyCount;
	private People2 father;

	@Valid
	@Value("#{father}")
	private People2 mother;

	@Valid
	@Autowired(required = false)
	@Qualifier("first")
	private People2 mother2;

	@Value("${son.all}")
	@PeopleFormat2(separator = Separator.SLASH)
	private People2 son;

	@Value("${daughter.all}")
	@PeopleFormat2(separator = Separator.SLASH)
	private People2 daughter;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private Environment env;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ResourceLoader resourceLoader;

	// 需要*Aware接口
	private String beanName;
	private ClassLoader classLoader;
	private LoadTimeWeaver loadTimeWeaver;

	// a.Bean instantiation with a constructor
	// 1.Constructor-based dependency injection(byName with javac -g)
	@Autowired
	public Family2(@Value("${family.1.familyName:ruan_wei}") String familyName,
			@Value("${family.familyCount:4}") int familyCount, @Valid People2 father) {
		this.familyName = familyName;
		this.familyCount = familyCount;
		this.father = father;
		log.info("Family2(String familyName, int familyCount, People father)" + this);
	}

	// 3.Method injection: Lookup method injection
	@Lookup("father")
	protected People2 createGuest() {
		log.info("createGuest");
		return null;
	}

	// 3.Method injection: Arbitrary method replacement
	protected int calc(int a, int b) {
		return a + b;
	}

	public void sayHello(String message) {
		log.info("sayHello(String message)" + message);

		log.info("3 + 5 = " + calc(3, 5));

		People2 guest = createGuest();
		ApplicationEvent event = new MyApplicationEvent2(this, message);
		// 等价于
		// publisher.publishEvent(new
		// PayloadApplicationEvent<People2>(this,guest));
		// context.publishEvent(new
		// PayloadApplicationEvent<String>(this,guest));
		publisher.publishEvent(guest);
		publisher.publishEvent(event);
		context.publishEvent(guest);
		context.publishEvent(event);

		context.getBean("house", AbsHouse2.class).greeting("whatever");

		if (messageSource == null) {
			messageSource = (MessageSource) context;
		}
		log.info("messageSource==========" + messageSource);
		String msg = messageSource.getMessage("my.messageSource", new Object[] { "ruanwei" },
				"This is my message source.", Locale.US);
		log.info("message==========" + msg);

		if (resourceLoader == null) {
			resourceLoader = (ResourceLoader) context;
		}
		log.info("resourceLoader==========" + resourceLoader);
		Resource resource = resourceLoader.getResource("spring/applicationContext.xml");
		log.info("resource==========" + resource);

		if (env == null) {
			env = context.getEnvironment();
		}
		log.info("env==========" + env);
		String a = env.getProperty("guest.name"); // @Value才可以取到PropertySourcesPlaceholderConfigurer的值
		String b = env.getProperty("b"); // -Db=3 MapPropertySource(systemProperties)/SystemEnvironmentPropertySource(systemEnvironment)
		String c = env.getProperty("p.username");// ResourcePropertySource(@PeopertySource("peopertySource.properties"))
		log.info("property=========a=" + a + " b=" + b + " c=" + c);
	}

	// 2.Setter-based dependency injection
	@Override
	public void setBeanName(String name) {
		log.info("setBeanName(String name)" + name);
		this.beanName = name;
	}

	@Override
	public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
		log.info("setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver)" + loadTimeWeaver);
		this.loadTimeWeaver = loadTimeWeaver;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		log.info("setBeanClassLoader(ClassLoader classLoader)" + classLoader);
		this.classLoader = classLoader;
	}

	// JSR-250.Initialization callback.等价于<bean init-method="init"/>.
	@PostConstruct
	public void init() {
		log.info("====================init()");
	}

	// JSR-250.Destruction callback.等价于<bean destroy-method="destroy"/>.
	@PreDestroy
	public void destroy() {
		log.info("====================destroy()");
	}
	
	@Override
	public String toString() {
		return "Family2 [familyName=" + familyName + ", familyCount=" + familyCount + ", father=" + father + ", mother="
				+ mother + ", mother2=" + mother2 + ", son=" + son + ", daughter=" + daughter + "]";
	}

}
