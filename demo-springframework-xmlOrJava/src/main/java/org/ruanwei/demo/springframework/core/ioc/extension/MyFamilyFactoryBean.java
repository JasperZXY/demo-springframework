package org.ruanwei.demo.springframework.core.ioc.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.springframework.beans.factory.FactoryBean;

public class MyFamilyFactoryBean implements FactoryBean<Family> {
	private static Log log = LogFactory.getLog(MyFamilyFactoryBean.class);

	private String familyName;
	private int familyCount;

	public MyFamilyFactoryBean(String familyName, int familyCount) {
		this.familyName = familyName;
		this.familyCount = familyCount;
		log.info("MyFamilyFactoryBean(String familyName, int familyCount)" + this);
	}

	@Override
	public Family getObject() throws Exception {
		Family family = new Family(this.familyName, this.familyCount, null);
		log.info("getObject()" + family);
		return family;
	}

	@Override
	public Class<Family> getObjectType() {
		log.info("getObjectType()");
		return Family.class;
	}

	@Override
	public boolean isSingleton() {
		log.info("isSingleton()");
		return true;
	}

	@Override
	public String toString() {
		return "MyFamilyFactoryBean [familyName=" + familyName + ", familyCount=" + familyCount + "]";
	}

}
