package org.ruanwei.demo.springframework.core.ioc.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.Family3;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("familyx")
public class MyFamilyFactoryBean3 implements FactoryBean<Family3> {
	private static Log log = LogFactory.getLog(MyFamilyFactoryBean3.class);

	@Value("${family.x.familyName}")
	private String familyName;
	
	@Value("${family.familyCount}")
	private int familyCount;

	@Autowired
	public MyFamilyFactoryBean3(@Value("${family.x.familyName}") String familyName,
			@Value("${family.familyCount}") int familyCount) {
		this.familyName = familyName;
		this.familyCount = familyCount;
		log.info("MyFamilyFactoryBean(String familyName, int familyCount)" + this);
	}

	@Override
	public Family3 getObject() throws Exception {
		log.info("getObject()");
		Family3 family = new Family3(this.familyName, this.familyCount, null);
		return family;
	}

	@Override
	public Class<?> getObjectType() {
		log.info("getObjectType()");
		return Family3.class;
	}

	@Override
	public boolean isSingleton() {
		log.info("isSingleton()");
		return true;
	}
	
	@Override
	public String toString() {
		return "MyFamilyFactoryBean3 [familyName=" + familyName + ", familyCount=" + familyCount + "]";
	}

}
