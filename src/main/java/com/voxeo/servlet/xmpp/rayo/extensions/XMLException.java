package com.voxeo.servlet.xmpp.rayo.extensions;

@SuppressWarnings("serial")
public class XMLException extends Exception {

	public XMLException() {
		super();
	}

	public XMLException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMLException(String message) {
		super(message);
	}

	public XMLException(Throwable cause) {
		super(cause);
	}
}
