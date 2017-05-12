package edu.iscas.expdroid.exception;

public class MessageNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private int errorCode;
    public MessageNotFoundException(String detailMessage){
    	super(detailMessage);
    }
}
