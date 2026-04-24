package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7267963160306970805L;
	
	public ResourceAlreadyExistsException(String resource,String fieldName,String fieldValue) {
		super(String.format("%s is already exists with value %s : %s",resource,fieldName,fieldValue ));
	}
}