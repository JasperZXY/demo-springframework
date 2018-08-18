package org.ruanwei.demo.springframework.core.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("aopService")
public class AopService {
	private static Log log = LogFactory.getLog(AopService.class);
	
	private ApplicationContext context;

	public  void testAop() {
		log.info("1======================================================================================");
		testAop(context);
	}

	private  void testAop(ApplicationContext context) {
		Family family = context.getBean("family", Family.class);
		family.sayHello("whatever");

		Good2 good = (Good2) context.getBean("good");
		Happy2 mixin = (Happy2) context.getBean("good");
		log.info(good.good("whatever") + mixin.happy("whatever"));
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}
}
