package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class PeopleFormatterRegistrar3 implements FormatterRegistrar {
	private static Log log = LogFactory.getLog(PeopleFormatterRegistrar3.class);

	public PeopleFormatterRegistrar3() {
		log.info("PeopleFormatterRegistrar3()");
	}

	@Override
	public void registerFormatters(FormatterRegistry registry) {
		log.info("registerFormatters(FormatterRegistry registry)" + registry);
		registry.addConverter(new StringToPeopleConverter3());
		// registry.addConverterFactory(new StringToPeopleConverterFactory3());
		registry.addFormatter(new PeopleFormatter3("/"));
		registry.addFormatterForFieldType(People3.class, new PeopleFormatter3("/"));
		registry.addFormatterForFieldAnnotation(new PeopleFormatAnnotationFormatterFactory3());
	}

}
