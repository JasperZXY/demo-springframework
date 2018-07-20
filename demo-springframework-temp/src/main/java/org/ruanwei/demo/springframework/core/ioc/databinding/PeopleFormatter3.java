package org.ruanwei.demo.springframework.core.ioc.databinding;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.springframework.format.Formatter;

/**
 * 
 * @author Administrator
 *
 */
final class PeopleFormatter3 implements Formatter<People3> {
	private static Log log = LogFactory.getLog(PeopleFormatter3.class);

	private String delimiter;

	public PeopleFormatter3() {
		log.info("PeopleFormatter3()");
		this.delimiter = "/";
	}

	public PeopleFormatter3(String delimiter) {
		log.info("PeopleFormatter3(String delimiter)" + delimiter);
		this.delimiter = delimiter;
	}

	@Override
	public String print(People3 people, Locale locale) {
		log.info("print(People3 people, Locale locale) " + people);
		if (people == null) {
			return "";
		}
		return people.toString();
	}

	@Override
	public People3 parse(String text, Locale locale) throws ParseException {
		log.info("parse(String text, Locale locale) " + text);
		if (text == null || text.isEmpty()) {
			return null;
		}
		String[] kv = text.split(delimiter);
		return new People3(kv[0], Integer.parseInt(kv[1]));
	}

}
