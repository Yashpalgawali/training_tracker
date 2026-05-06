package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TestingScheduleDto;
import com.example.demo.entity.TestingSchedule;
import com.example.demo.service.ITestingScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("testingschedule")
@RequiredArgsConstructor
@Tag(name = "Testing Schedule Controller", description = "This controller handles endpoints to perform operations like Find the Testing Schedule")
public class TestingScheduleController {

	private final ITestingScheduleService testingscheduleserv;

//	@GetMapping("/")
//	@Operation(summary = "Find List of Testing Schedule", description = "This endpoint finds the List of Testing Schedule Schedulefrom the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule Scheduleare found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Testing Schedule Scheduleare found") })
//	public ResponseEntity<List<TestingSchedule>> getAllFrequencies() {
//		var testingList = testingscheduleserv.getAllTestings();
//		return ResponseEntity.status(HttpStatus.OK).body(testingList);
//	}
//	
	@PostMapping("/")
	@Operation(summary = "Save the Testing Schedule", description = "This endpoint saved the Testing Schedule in the Database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Testing Schedule Scheduleare found") })
	public ResponseEntity<ResponseDto> saveTesting(@RequestBody TestingScheduleDto testingSchedule) {
		testingscheduleserv.saveTestingSchedule(testingSchedule);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Testing Scheduled dated on "+testingSchedule.getTestingScheduleDate()));
	}
//	
//	@GetMapping("/{id}")
//	@Operation(summary = "Find the Testing Schedule Scheduleusing ID", description = "This endpoint finds the Testing Schedule Schedulefrom the database by using its ID")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule Scheduleis found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Testing Schedule Scheduleis found") })
//	public ResponseEntity<Testing Schedule> getTestingById(@PathVariable Long id) {
//		var testing = testingscheduleserv.getTestingById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(testing );
//	}
	
	
	@GetMapping("/year/{year}")
	@Operation(summary = "Find the Testing Schedule using Year", description = "This endpoint finds the Testing Schedule from the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Testing Schedule is found for the year") })
	public ResponseEntity<List<TestingSchedule>> getTestingScheduleByYear(@PathVariable String year) {
		var testing = testingscheduleserv.getTestingScheduleByYear(year);
		return ResponseEntity.status(HttpStatus.OK).body(testing );
	}
//	
//	@PutMapping("/")
//	@Operation(summary = "Update the Testing Schedule", description = "This endpoint updates the Testing Schedule Schedulein the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule updated Successfully "),
//			@ApiResponse(responseCode = "404", description = "Testing Schedule Scheduleis not updated") })
//	public ResponseEntity<ResponseDto> updateTesting(@RequestBody Testing Schedule Scheduletesting) {
//		testingscheduleserv.updateTesting(testing);
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Testing Schedule Schedule"+testing.getTestingName()+" is updated successfully"));
//	}
}
