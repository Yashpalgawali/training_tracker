package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Test;
import com.example.demo.service.ITestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
@Tag(name = "Test Controller", description = "This controller handles endpoints to perform operations like Save Test,Find, Update the test")
public class TestController {

	private final ITestService testserv;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/")
	@Operation(summary = "Save Test", description = "This endpoint saves the Test object to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "The test is saved Successfully "),
			@ApiResponse(responseCode = "500", description = "The test is NOT Saved ") })
	public ResponseEntity<ResponseDto> saveCompany(@RequestBody Test test) {
		testserv.saveTest(test);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Test " + test.getTestName() + " is saved Successfully"));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find Test By ID", description = "This endpoint finds the Test object from the database by its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The test is found Successfully "),
			@ApiResponse(responseCode = "404", description = "The test is NOT FOUND") })
	public ResponseEntity<Test> getCompanyById(@PathVariable("id") Long testid) {
		var test = testserv.getTestById(testid);
		return ResponseEntity.status(HttpStatus.OK).body(test);
	}

//	@GetMapping("/")
//	@Operation(summary = "Find List of Categories", description = "This endpoint finds the List of Categories from the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The companies are found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No companies are found") })
//	public ResponseEntity<List<Test>> getAllCategories() {
//		var test = testserv.getAllCategories();
//		return ResponseEntity.status(HttpStatus.OK).body(test);
//	}

	@PutMapping("/")
	@Operation(summary = "Update Test", description = "This endpoint UPDATES the Test object to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The test is updated Successfully "),
			@ApiResponse(responseCode = "304", description = "The test is NOT updated ") })
	public ResponseEntity<ResponseDto> updateCompany(@RequestBody Test test) {
		testserv.updateTest(test);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Test " + test.getTestName() + " is Updated Successfully"));
	}
}
