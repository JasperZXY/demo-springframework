package org.ruanwei.demo.springframework.core.ioc.databinding;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People2;

public class PeoplePropertyEditor2 extends PropertyEditorSupport {
	private static Log log = LogFactory.getLog(PeoplePropertyEditor2.class);

	public PeoplePropertyEditor2() {
		log.info("PeoplePropertyEditor2()");
	}

	@Override
	public void setAsText(String text) {
		log.info("setAsText(String text)" + text);
		String[] kv = text.split("/");
		setValue(new People2(kv[0], Integer.parseInt(kv[1])));
	}

	@Override
	public String getAsText() {
		log.info("getAsText()");
		People2 poeple = (People2) getValue();
		return poeple.toString();
	}
}
