package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.NOT_FOUND,
													exception.getMessage(),LocalDateTime.now().toString() );
		
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponseDto> handleGlobaldException(GlobalException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.INTERNAL_SERVER_ERROR,
													exception.getMessage(),LocalDateTime.now().toString() );
		
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	@ExceptionHandler(ResourceNotModifiedException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotModifiedException(ResourceNotModifiedException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.CONFLICT,
													exception.getMessage(),LocalDateTime.now().toString() );
		
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
}
