package org.ruanwei.demo.springframework.core.ioc.event;

import org.springframework.context.ApplicationEvent;

@Deprecated
public class MyApplicationEvent3 extends ApplicationEvent {
	private String message;

	public MyApplicationEvent3(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
