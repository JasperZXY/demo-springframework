# demo-springframework

### demo-springframework项目共包含两个模块：
- demo-springframework-withoutAnnotation模块为纯基于XML/Java的配置元数据的项目，未开启基于注解的配置元数据。
- demo-springframework-withAnnotation模块为基于XML/Java的配置元数据的项目，开启基于注解的配置元数据(<context:annotation-config/>)。

### 基于XML和基于Java的配置元数据主要对比：
- &lt;beans> vs @Configuration.
- &lt;beans profile="dev"/> vs @Profile("dev").
- &lt;import resource="classpath:dataAccess.xml"> vs @Import(DadaAccessConfig.class).
- &lt;bean class="DadaAccessConfig.class"> vs @ImportResource("classpath:dataAccess.xml").
- &lt;bean id="myBean" class="org.ruanwei.MyBean"> vs @Bean("myBean").
- nothing vs @Description("this is a bean").
- &lt;bean lazy-init-"true"> vs @Lazy.
- &lt;bean depends-on-"anotherBean"> vs @DependsOn("anotherBean").
- &lt;bean scope="singleton"> vs @Scope("singleton") vs JSR-330:@Singleton/@Scope.
- &lt;bean init-method="init"> vs @Bean(init-method="init").
- &lt;bean destroy-method="destroy"> vs @Bean(destroy-method="destroy").
- &lt;bean p:order="1"> vs @Order(1) vs JSR-250:@Priority(1).
- &lt;bean primary="true"> vs @Primary.
- &lt;bean>&lt;qualifier value="primaryBean"/>&lt;/bean> vs @Qualifier("primaryBean") vs JSR-330:@Named("primaryBean")/@Qualifier.
- &lt;bean>&lt;lookup-method name="createCommand" bean="myCommand"/>&lt;/bean> vs nothing.
- &lt;bean>&lt;replaced-method name="computeValue" replacer="replacementComputeValue"/>&lt;/bean> vs nothing.
- nothing vs @PropertySource("classpath:ps.properties").
- &lt;aop:aspectj-autoproxy/> vs @EnableAspectJAutoProxy.

### 开启基于注解的配置元数据：
- &lt;context:annotation-config/> vs @Bean xxxBeanPostProcessor.
- &lt;context:component-scan="org.ruanwei"/> vs @ComponentScan("org.ruanwei").
<p>注意：基于XML的配置元数据要支持@Configuration注解需要使用&lt;context:annotation-config/>开启.

### 基于注解和非基于注解的配置元数据主要对比：
- @Component("myBean")/JSR-250:@ManagedBean("myBean") vs @Bean("myBean").
- @Required vs nothing.
- @Autowired(required="true")/JSR-250:@Resource("myBean")/JSR-330:@Inject vs nothing.
- @Value("${holder}") vs nothing.
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
