package org.ruanwei.demo.springframework.core.ioc.databinding;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;

public class PeoplePropertyEditor3 extends PropertyEditorSupport {
	private static Log log = LogFactory.getLog(PeoplePropertyEditor3.class);

	public PeoplePropertyEditor3() {
		log.info("PeoplePropertyEditor3()");
	}

	@Override
	public void setAsText(String text) {
		log.info("setAsText(String text)" + text);
		String[] kv = text.split("/");
		setValue(new People3(kv[0], Integer.parseInt(kv[1])));
	}

	@Override
	public String getAsText() {
		log.info("getAsText()");
		People3 poeple = (People3) getValue();
		return poeple.toString();
	}
}
