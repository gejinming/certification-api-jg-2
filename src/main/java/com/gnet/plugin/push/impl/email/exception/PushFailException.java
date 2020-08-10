package com.gnet.plugin.push.impl.email.exception;

import javax.mail.MessagingException;

public class PushFailException extends MessagingException {

	private static final long serialVersionUID = -4505568240185990545L;

	public PushFailException() {
		super();
	}

	public PushFailException(String s) {
		super(s);
	}
	
}
