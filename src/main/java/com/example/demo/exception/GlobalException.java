package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6077624623096866881L;

	public GlobalException(String msg) {
		super(msg);
	}
}
