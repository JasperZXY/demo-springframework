package org.ruanwei.demo.springframework.core.ioc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FamilyFactory2 {
	private static Log log = LogFactory.getLog(FamilyFactory2.class);

	// 2 Bean instantiation with a static factory method
	public static Family2 createInstance1(String familyName, int familyCount, People2 father) {
		log.info("createInstance1(String familyName, int familyCount, People father)" + familyName + familyCount
				+ father);
		return new Family2(familyName, familyCount, father);
	}

	// 3 Bean instantiation using an instance factory method
	public Family2 createInstance2(String familyName, int familyCount, People2 father) {
		log.info("createInstance2(String familyName, int familyCount, People father)" + familyName + familyCount
				+ father);

		return createInstance1(familyName, familyCount, father);
	}

}
