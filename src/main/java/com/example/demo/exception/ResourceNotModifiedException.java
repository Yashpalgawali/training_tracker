package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceNotModifiedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7267963160306970805L;

	public ResourceNotModifiedException(String msg) {
		super(msg);
	}	
}