package com.example.demo.dto;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class ErrorResponseDto {

	String apiPath;

	HttpStatus errorCode;

	String errorMessage;

	String errorTime;
}
