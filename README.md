# demo-springframework

### demo-springframework项目共包含两个模块：
- demo-springframework-withoutAnnotation模块为纯基于XML/Java的配置元数据的项目，未开启基于注解的配置元数据。
- demo-springframework-withAnnotation模块为基于XML/Java的配置元数据的项目，并开启基于注解的配置元数据(<context:annotation-config/>)。

### 基于XML和基于Java的配置元数据主要对比：
- &lt;beans> vs @Configuration.
- &lt;beans profile="dev"/> vs @Profile("dev").
- &lt;beans default-lazy-init="true"/> vs nothing.
- &lt;beans default-autowire="byName"/> vs nothing.
- &lt;beans default-autowire-candidates="*Service"/> vs nothing.
- &lt;import resource="classpath:dataAccess.xml"> vs @Import(DadaAccessConfig.class).
- &lt;bean class="DadaAccessConfig.class"> vs @ImportResource("classpath:dataAccess.xml").
- ctx.getEnvironment().getPropertySources().addFirst(new ResourcePropertySource("ps.properties")) vs @PropertySource("classpath:ps.properties").
- &lt;bean id="myBean" class="org.ruanwei.MyBean"> vs @Bean("myBean").
- nothing vs @Description("this is a bean").
- &lt;bean lazy-init-"true"> vs @Lazy.
- &lt;bean depends-on-"anotherBean"> vs @DependsOn("anotherBean").
- &lt;bean scope="singleton"> vs @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) vs JSR-330:@Singleton/@Scope.
- &lt;bean init-method="init"> vs InitializingBean vs @Bean(initMethod="init").
- &lt;bean destroy-method="destroy"> vs DisposableBean vs @Bean(destroyMethod="destroy").
- &lt;bean p:order="1">(PriorityOrdered/Ordered) vs @Order(1) vs JSR-250:@Priority(1).
- &lt;bean autowire="byType"> vs @Bean(autowire=Autowire.BY_TYPE).
- &lt;bean primary="true"> vs @Primary.
- &lt;bean autowire-candidate="false"> vs nothing.
- &lt;bean>&lt;qualifier value="primaryBean"/>&lt;/bean> vs @Qualifier("primaryBean") vs JSR-330:@Named("primaryBean")/@Qualifier.
- &lt;bean>&lt;lookup-method name="createCommand" bean="myCommand"/>&lt;/bean> vs nothing.
- &lt;bean>&lt;replaced-method name="computeValue" replacer="replacementComputeValue"/>&lt;/bean> vs nothing.
- &lt;context:load-time-weaver/> vs @EnableLoadTimeWeaving.
- &lt;context:spring-configured/> vs @EnableSpringConfigured.

- &lt;aop:aspectj-autoproxy/> vs @EnableAspectJAutoProxy.
- &lt;aop:scoped-proxy proxy-target-class="true"/> vs @Scope(proxyMode=ScopedProxyMode.TARGET_CLASS).
<p>注意：基于XML的配置元数据使用&lt;context:annotation-config/>开启@Configuration注解支持.

### 开启基于注解的配置元数据：
- &lt;context:annotation-config/> vs @Bean xxxBeanPostProcessor.
- &lt;context:component-scan base-package="org.ruanwei" scoped-proxy="class"/> vs @ComponentScan(basePackages="org.ruanwei",scopedProxy=ScopedProxyMode.CLASS).

### 基于注解和非基于注解的配置元数据主要对比：
- @Component("myBean")/JSR-250:@ManagedBean("myBean") vs @Bean("myBean").
- @Required vs nothing.
- @Autowired(required="true")/JSR-250:@Resource("myBean")/JSR-330:@Inject vs nothing.
- @Value("${placeholder}")/@Value("#{SpEL}") vs nothing.
- JSR-250:@PostConstruct vs @Bean(init-method="init").
- JSR-250:@PreDestroy vs @Bean(destroy-method="destroy").
- @EventListener vs org.springframework.context.event.EventListener.
- @Lookup vs &lt;lookup-method name="createCommand" bean="myCommand"/>.
- @PersistenceContext vs .

### TODO:
1. 补充在Spring中使用AspectJ；
2. 解析自定义配置xml标签
3. demo-springframework-temp为临时项目，准备删除。
4. 补充测试用例
5. 补充@Valid支持分组验证；
6. 以下考虑从XmlConfig项目中迁出：
- a.@Valid/@Validated注解；
- b.@Format注解；
7. 补充AOP和事务的配置对比
8. 事务和AOP的引入没有对应的Java配置没有对比
9. withoutAnnotation在java配置的测试用例空指针
