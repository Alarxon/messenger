package org.itver.com.messenger.exception;

public class DataNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 7749617550799595227L;
	
	public DataNotFoundException(String message) {
		super(message);
	}
}


