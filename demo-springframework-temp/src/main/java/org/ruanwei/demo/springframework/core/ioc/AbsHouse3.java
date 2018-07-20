package org.ruanwei.demo.springframework.core.ioc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class AbsHouse3 {
	private static Log log = LogFactory.getLog(AbsHouse3.class);

	@Value("${abshouse.name:ruan_wei}")
	private String houseName;

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

	@Value("#{someList2}")
	private List<Integer> someList2;

	@Value("#{someSet2}")
	private Set<Integer> someSet2;

	// 注意这里注入的是所有的KV
	@Value("#{someProperties2}")
	private Properties someProperties2;

	@Value("#{someMap2}")
	private Map<String, Integer> someMap2;
	
	@Value("#{systemProperties['java.version']?:'1.8'}")
	private String javaVersion;
	
	// JSR-250
	@Resource(name = "someList2")
	private List<Integer> someList3;

	@Resource(name = "someSet2")
	private Set<Integer> someSet3;

	// 注意这里注入的是所有的KV
	@Resource(name = "someProperties2")
	private Properties someProperties3;

	@Resource(name = "someMap2")
	private Map<String, Integer> someMap3;
	
	@Autowired
	private List<Integer> someList4;

	@Autowired
	private Set<Integer> someSet4;

	// 注意这里注入的是所有的KV
	// @Autowired
	private Properties someProperties4;

	@Autowired
	private Map<String, Integer> someMap4;

	// JSR-330
	@Inject
	private List<Integer> someList5;

	@Inject
	private Set<Integer> someSet5;

	// @Inject
	private Properties someProperties5;

	@Inject
	private Map<String, Integer> someMap5;

	@Resource(name = "someField1")
	private double someField1;

	@Resource(name = "someField2")
	private String someField2;

	@Resource(name = "someField3")
	private double someField3;

	// JSR-349:Bean Validation 1.1
	@NotNull
	public String greeting(@Size(min = 2, max = 8) String message) {
		log.info("greeting(String message)" + message);
		return message;
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
		return "AbsHouse3 [houseName=" + houseName + ", someArray=" + Arrays.toString(someArray) + ", someList="
				+ someList + ", someSet=" + someSet + ", someProperties=" + someProperties + ", someMap=" + someMap
				+ ", someList2=" + someList2 + ", someSet2=" + someSet2 + ", someProperties2=" + someProperties2
				+ ", someMap2=" + someMap2 + ", javaVersion=" + javaVersion + ", someList3=" + someList3 + ", someSet3="
				+ someSet3 + ", someProperties3=" + someProperties3 + ", someMap3=" + someMap3 + ", someList4="
				+ someList4 + ", someSet4=" + someSet4 + ", someProperties4=" + someProperties4 + ", someMap4="
				+ someMap4 + ", someList5=" + someList5 + ", someSet5=" + someSet5 + ", someProperties5="
				+ someProperties5 + ", someMap5=" + someMap5 + ", someField1=" + someField1 + ", someField2="
				+ someField2 + ", someField3=" + someField3 + "]";
	}
}
