package com.andy.gomoku.exception;

public class GoSeviceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public GoSeviceException() {
        super();
    }
	
	public GoSeviceException(Throwable cause) {
        super(cause);
    }
	
	public GoSeviceException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public GoSeviceException(String message) {
        super(message);
    }

}
