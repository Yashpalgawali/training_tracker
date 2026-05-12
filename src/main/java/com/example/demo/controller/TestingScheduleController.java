package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TestingScheduleDto ;
import com.example.demo.entity.TestSchedule;
import com.example.demo.service.ITestScheduleService;

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

	private final ITestScheduleService testingscheduleserv;

//	@GetMapping("/")
//	@Operation(summary = "Find List of Testing Schedule", description = "This endpoint finds the List of Testing Schedule Schedulefrom the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule Scheduleare found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Testing Schedule Scheduleare found") })
//	public ResponseEntity<List<TestSchedule>> getAllFrequencies() {
//		var testingList = testingscheduleserv.getAllTestings();
//		return ResponseEntity.status(HttpStatus.OK).body(testingList);
//	}
//	
	@PostMapping("/")
	@Operation(summary = "Save the Testing Schedule", description = "This endpoint saved the Testing Schedule in the Database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Testing Schedule Scheduleare found") })
	public ResponseEntity<ResponseDto> saveTestingSchedule(@RequestBody TestingScheduleDto  testingSchedule) {
		testingscheduleserv.saveTestSchedule(testingSchedule);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Testing Scheduled dated on "+testingSchedule.getTestScheduleDate() ));
	}
//	
//	@GetMapping("/{id}")
//	@Operation(summary = "Find the Testing Schedule Scheduleusing ID", description = "This endpoint finds the Testing Schedule Schedulefrom the database by using its ID")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule Scheduleis found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Testing Schedule is found") })
//	public ResponseEntity<Testing Schedule> getTestingById(@PathVariable Long id) {
//		var testing = testingscheduleserv.getTestingById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(testing );
//	}
	
	
	@GetMapping("/year/{year}")
	@Operation(summary = "Find the Test Schedule using Year", description = "This endpoint finds the Test Schedule from the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Test Schedule Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Test Schedule is found for the year") })
	public ResponseEntity<List<TestSchedule>> getTestByYear(@PathVariable String year) {
		var test = testingscheduleserv.getTestScheduleByYear(year);
		return ResponseEntity.status(HttpStatus.OK).body(test );
	}
	
	@PatchMapping("/year/{year}")
	public ResponseEntity<ResponseDto> updateSignaturesByYear(@PathVariable String year,
			@RequestBody Map<String, String> body) {
		
		testingscheduleserv.updateTestScheduleSignatureByYear(body, year);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(HttpStatus.OK.toString(),
						"The signature for year " + year
								+ " is updated Successfully"));
	}
	
//	@PutMapping("/")
//	@Operation(summary = "Update the Testing Schedule", description = "This endpoint updates the Testing Schedule Schedulein the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule updated Successfully "),
//			@ApiResponse(responseCode = "404", description = "Testing Schedule Scheduleis not updated") })
//	public ResponseEntity<ResponseDto> updateTesting(@RequestBody Testing Schedule Scheduletesting) {
//		testingscheduleserv.updateTesting(testing);
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Testing Schedule Schedule"+testing.getTestingName()+" is updated successfully"));
//	}
}
