package org.ruanwei.demo.springframework.core.ioc.databinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People2;
import org.ruanwei.demo.springframework.core.ioc.databinding.PeopleFormat2.Separator;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * 
 * @author Administrator
 *
 */
public final class PeopleFormatAnnotationFormatterFactory2 implements AnnotationFormatterFactory<PeopleFormat2> {
	private static Log log = LogFactory.getLog(PeopleFormatAnnotationFormatterFactory2.class);

	public PeopleFormatAnnotationFormatterFactory2() {
		log.info("PeopleFormatAnnotationFormatterFactory2()");
	}

	@Override
	public Set<Class<?>> getFieldTypes() {
		log.info("getFieldTypes() ");
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { People2.class }));
	}

	@Override
	public Printer<People2> getPrinter(PeopleFormat2 annotation, Class<?> fieldType) {
		log.info("getPrinter(MyPeopleFormat annotation, Class<?> fieldType)");
		return configureFormatterFrom(annotation, fieldType);
	}

	@Override
	public Parser<People2> getParser(PeopleFormat2 annotation, Class<?> fieldType) {
		log.info("getParser(MyPeopleFormat annotation, Class<?> fieldType)");
		return configureFormatterFrom(annotation, fieldType);
	}

	private Formatter<People2> configureFormatterFrom(PeopleFormat2 annotation, Class<?> fieldType) {
		Separator separator = annotation.separator();
		if (separator == Separator.SLASH) {
			return new PeopleFormatter2("/");
		} else if (separator == Separator.COMMA) {
			return new PeopleFormatter2(",");
		} else {
			return new PeopleFormatter2("/");
		}
	}

}
