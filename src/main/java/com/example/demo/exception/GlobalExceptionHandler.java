package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.NOT_FOUND,
													exception.getMessage(),LocalDateTime.now().toString() );
		
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.BAD_REQUEST,
													exception.getMessage(),LocalDateTime.now().toString() );		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException exception , WebRequest request){
		
		ErrorResponseDto error = new ErrorResponseDto(
													request.getDescription(false),
													HttpStatus.UNAUTHORIZED,
													exception.getMessage(),LocalDateTime.now().toString() );
		
		System.err.println("Error is "+error.toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
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
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String , String> validationErrors = new HashMap<>();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
		
		validationErrorList.forEach((error)->{
			String fieldName = ((FieldError)error).getField();
			String validationMsg = error.getDefaultMessage();
			validationErrors.put(fieldName, validationMsg);
			
		});

		return new ResponseEntity<>(validationErrors,HttpStatus.BAD_REQUEST);
	}
}
