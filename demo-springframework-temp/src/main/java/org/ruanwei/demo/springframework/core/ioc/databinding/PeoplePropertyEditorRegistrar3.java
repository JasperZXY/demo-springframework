package org.ruanwei.demo.springframework.core.ioc.databinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.People3;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class PeoplePropertyEditorRegistrar3 implements PropertyEditorRegistrar {
	private static Log log = LogFactory.getLog(PeoplePropertyEditorRegistrar3.class);

	public PeoplePropertyEditorRegistrar3() {
		log.info("PeoplePropertyEditorRegistrar3()");
	}

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		log.info("registerCustomEditors(PropertyEditorRegistry registry)" + registry);

		registry.registerCustomEditor(People3.class, new PeoplePropertyEditor3());
	}

}
