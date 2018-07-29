package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @see StringToNumberConverterFactory.
 * 
 * @author Administrator
 *
 */
final class StringToPeopleConverterFactory2 implements ConverterFactory<String, People2> {
	private static Log log = LogFactory.getLog(StringToPeopleConverterFactory2.class);

	public StringToPeopleConverterFactory2() {
		log.info("StringToPeopleConverter()");
	}

	@Override
	public <T extends People2> Converter<String, T> getConverter(Class<T> targetType) {
		return null;
	}

}
