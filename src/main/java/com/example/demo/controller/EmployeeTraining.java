package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;


@RestController
@RequestMapping("employee-training")
public class EmployeeTraining {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> assignTrainingToEmployee(@RequestBody EmployeeTraining emptrain) {
		logger.info("Employee Trainning Object is {} ",emptrain);
		return null;
	}
}
