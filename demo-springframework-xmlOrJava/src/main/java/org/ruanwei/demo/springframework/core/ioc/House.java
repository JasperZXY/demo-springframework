package org.ruanwei.demo.springframework.core.ioc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

// @Validated
public class House {
	private static Log log = LogFactory.getLog(House.class);

	private String houseName;
	private String hostName;

	private Integer[] someArray;
	private List<Integer> someList;
	private Set<Integer> someSet;
	private Properties someProperties;
	private Map<String, Integer> someMap;

	private List<Integer> someList2;
	private Set<Integer> someSet2;
	// 注意这里注入的是所有的KV
	private Properties someProperties2;
	private Map<String, Integer> someMap2;

	private double someField1;
	private String someField2;
	private String someField3;

	// Setter-based dependency injection
	@Required
	public void setHouseName(String houseName) {
		log.info("setHouseName(String houseName)" + houseName);
		this.houseName = houseName;
	}

	@Required
	public void setHostName(String hostName) {
		log.info("setHostName(String hostName)" + hostName);
		this.hostName = hostName;
	}

	@Required
	public void setSomeArray(Integer[] someArray) {
		log.info("setSomeArray(Integer[] someArray)" + someArray);
		this.someArray = someArray;
	}
	
	@Required
	public void setSomeList(List<Integer> someList) {
		log.info("setSomeList(List<Integer> someList)" + someList);
		this.someList = someList;
	}

	@Required
	public void setSomeSet(Set<Integer> someSet) {
		log.info("setSomeSet(Set<Integer> someSet)" + someSet);
		this.someSet = someSet;
	}

	@Required
	public void setSomeProperties(Properties someProperties) {
		log.info("setSomeProperties(Properties someProperties)" + someProperties);
		this.someProperties = someProperties;
	}

	@Required
	public void setSomeMap(Map<String, Integer> someMap) {
		log.info("setSomeMap(Map<String, Integer> someMap)" + someMap);
		this.someMap = someMap;
	}

	@Required
	public void setSomeList2(List<Integer> someList) {
		log.info("setSomeList2(List<Integer> someList)" + someList);
		this.someList2 = someList;
	}

	@Required
	public void setSomeSet2(Set<Integer> someSet) {
		log.info("setSomeSet2(Set<Integer> someSet)" + someSet);
		this.someSet2 = someSet;
	}

	@Required
	public void setSomeProperties2(Properties someProperties) {
		log.info("setSomeProperties2(Properties someProperties)" + someProperties);
		this.someProperties2 = someProperties;
	}

	@Required
	public void setSomeMap2(Map<String, Integer> someMap) {
		log.info("setSomeMap2(Map<String, Integer> someMap)" + someMap);
		this.someMap2 = someMap;
	}

	@Required
	public void setSomeField1(double someField) {
		log.info("setSomeField1(double someField)" + someField);
		this.someField1 = someField;
	}

	@Required
	public void setSomeField2(String someField) {
		log.info("setSomeField2(String someField)" + someField);
		this.someField2 = someField;
	}

	@Required
	public void setSomeField3(String someField) {
		log.info("setSomeField3(String someField)" + someField3);
		this.someField3 = someField;
	}

	public void init() {
		log.info("====================init()");
	}

	public void destroy() {
		log.info("====================destroy()");
	}

	@Override
	public String toString() {
		return "House [houseName=" + houseName + ", hostName=" + hostName + ", someArray=" + Arrays.toString(someArray)
				+ ", someList=" + someList + ", someSet=" + someSet + ", someProperties=" + someProperties
				+ ", someMap=" + someMap + ", someList2=" + someList2 + ", someSet2=" + someSet2 + ", someProperties2="
				+ someProperties2 + ", someMap2=" + someMap2 + ", someField1=" + someField1 + ", someField2="
				+ someField2 + ", someField3=" + someField3 + "]";
	}

}
