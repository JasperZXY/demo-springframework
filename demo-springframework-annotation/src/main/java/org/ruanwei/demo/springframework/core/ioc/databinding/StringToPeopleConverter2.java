package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People2;
import org.springframework.core.convert.converter.Converter;

/**
 * Take care to ensure that your Converter implementation is thread-safe.
 * 
 */
final class StringToPeopleConverter2 implements Converter<String, People2> {
	private static Log log = LogFactory.getLog(StringToPeopleConverter2.class);

	public StringToPeopleConverter2() {
		log.info("StringToPeopleConverter2()");
	}

	@Override
	public People2 convert(String source) {
		log.info("convert(String source)" + source);
		if (source == null || source.isEmpty()) {
			return null;
		}
		String[] kv = source.split("/");
		return new People2(kv[0], Integer.parseInt(kv[1]));
	}

}
