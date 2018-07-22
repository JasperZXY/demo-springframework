package org.ruanwei.demo.springframework.core.ioc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("development")
@Component("house")
public class House2_1 extends AbsHouse2 {
	private static Log log = LogFactory.getLog(House2_1.class);

	@Value("${house.name:houseName_def}")
	private String houseName;

	@Value("${house.host.development:development_def}")
	private String hostName;

	// JSR-250.Initialization callback.等价于<bean init-method="init"/>.
	@PostConstruct
	@Override
	public void init() {
		log.info("====================init()");
		super.init();
	}

	// JSR-250.Destruction callback.等价于<bean destroy-method="destroy"/>.
	@PreDestroy
	@Override
	public void destroy() {
		log.info("====================destroy()");
		super.destroy();
	}

	@Override
	public String toString() {
		return "House2_1 [hostName=" + hostName + "]" + super.toString();
	}

}
