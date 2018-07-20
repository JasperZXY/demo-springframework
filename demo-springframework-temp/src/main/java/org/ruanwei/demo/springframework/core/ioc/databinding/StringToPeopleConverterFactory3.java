package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @see StringToNumberConverterFactory.
 * 
 * @author Administrator
 *
 */
final class StringToPeopleConverterFactory3 implements ConverterFactory<String, People3> {
	private static Log log = LogFactory.getLog(StringToPeopleConverterFactory3.class);

	public StringToPeopleConverterFactory3() {
		log.info("StringToPeopleConverter3()");
	}

	@Override
	public <T extends People3> Converter<String, T> getConverter(Class<T> targetType) {
		return null;
	}

}
