package org.ruanwei.demo.springframework.core.ioc.event;

import org.springframework.context.ApplicationEvent;

@Deprecated
public class MyApplicationEvent2 extends ApplicationEvent {
	private String message;

	public MyApplicationEvent2(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
