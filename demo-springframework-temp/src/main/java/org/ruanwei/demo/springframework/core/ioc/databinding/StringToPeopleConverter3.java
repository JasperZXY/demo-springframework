package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.springframework.core.convert.converter.Converter;

/**
 * Take care to ensure that your Converter implementation is thread-safe.
 * 
 */
final class StringToPeopleConverter3 implements Converter<String, People3> {
	private static Log log = LogFactory.getLog(StringToPeopleConverter3.class);

	public StringToPeopleConverter3() {
		log.info("StringToPeopleConverter3()");
	}

	@Override
	public People3 convert(String source) {
		log.info("convert(String source)" + source);
		if (source == null || source.isEmpty()) {
			return null;
		}
		String[] kv = source.split("/");
		return new People3(kv[0], Integer.parseInt(kv[1]));
	}

}
