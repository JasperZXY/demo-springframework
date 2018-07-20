package org.ruanwei.demo.springframework.core.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class HappyImpl3 implements Happy3 {
	private static Log log = LogFactory.getLog(HappyImpl3.class);

	@Override
	public String happy(String msg) {
		log.info("happy()" + msg);
		return "Happy " + msg;
	}

}
