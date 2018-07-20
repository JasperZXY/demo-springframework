package org.ruanwei.demo.springframework.core.ioc.databinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormat3.Separator;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * 
 * @author Administrator
 *
 */
final class PeopleFormatAnnotationFormatterFactory3 implements AnnotationFormatterFactory<PeopleFormat3> {
	private static Log log = LogFactory.getLog(PeopleFormatAnnotationFormatterFactory3.class);

	public PeopleFormatAnnotationFormatterFactory3() {
		log.info("PeopleFormatAnnotationFormatterFactory3()");
	}

	@Override
	public Set<Class<?>> getFieldTypes() {
		log.info("getFieldTypes() ");
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { People3.class }));
	}

	@Override
	public Printer<People3> getPrinter(PeopleFormat3 annotation, Class<?> fieldType) {
		log.info("getPrinter(MyPeopleFormat3 annotation, Class<?> fieldType)");
		return configureFormatterFrom(annotation, fieldType);
	}

	@Override
	public Parser<People3> getParser(PeopleFormat3 annotation, Class<?> fieldType) {
		log.info("getParser(MyPeopleFormat3 annotation, Class<?> fieldType)");
		return configureFormatterFrom(annotation, fieldType);
	}

	private Formatter<People3> configureFormatterFrom(PeopleFormat3 annotation, Class<?> fieldType) {
		Separator separator = annotation.separator();
		if (separator == Separator.SLASH) {
			return new PeopleFormatter3("/");
		} else if (separator == Separator.COMMA) {
			return new PeopleFormatter3(",");
		} else {
			return new PeopleFormatter3("/");
		}
	}

}
