package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.CommitteeSchedule;
import com.example.demo.service.ICommitteeScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("committeeschedule")
@RequiredArgsConstructor
@Tag(name = "Committee Schedule ScheduleController", description = "This controller handles endpoints to perform operations like Find the Committee Schedule")
public class CommitteeScheduleController {

	private final ICommitteeScheduleService committeescheduleserv;

//	@GetMapping("/")
//	@Operation(summary = "Find List of Committee Schedule", description = "This endpoint finds the List of Committee Schedule Schedulefrom the database")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee Schedule Scheduleare found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Committee Schedule Scheduleare found") })
//	public ResponseEntity<List<CommitteeSchedule>> getAllFrequencies() {
//		var committeeList = committeescheduleserv.getAllCommittees();
//		return ResponseEntity.status(HttpStatus.OK).body(committeeList);
//	}
//	
	@PostMapping("/")
	@Operation(summary = "Save the Committee Schedule", description = "This endpoint saved the Committee Schedule Schedulein the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee Schedule Scheduleis saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Committee Schedule Scheduleare found") })
	public ResponseEntity<ResponseDto> saveCommittee(@RequestBody CommitteeScheduleDto committeeSchedule) {
		committeescheduleserv.saveCommitteeSchedule(committeeSchedule);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Committee Scheduled dated on "+committeeSchedule.getCommitteeScheduleDate()));
	}
//	
//	@GetMapping("/{id}")
//	@Operation(summary = "Find the Committee Schedule Scheduleusing ID", description = "This endpoint finds the Committee Schedule Schedulefrom the database by using its ID")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee Schedule Scheduleis found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Committee Schedule Scheduleis found") })
//	public ResponseEntity<Committee Schedule> getCommitteeById(@PathVariable Long id) {
//		var committee = committeescheduleserv.getCommitteeById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(committee );
//	}
	
	
	@GetMapping("/year/{year}")
	@Operation(summary = "Find the Committee Schedule using Year", description = "This endpoint finds the Committee Schedule Schedulefrom the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee  Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Committee Schedule is found for the year") })
	public ResponseEntity<List<CommitteeSchedule>> getCommitteeByYear(@PathVariable String year) {
		var committee = committeescheduleserv.getCommitteeScheduleByYear(year);
		return ResponseEntity.status(HttpStatus.OK).body(committee );
	}
	
	@PatchMapping("/year/{year}")
	public ResponseEntity<ResponseDto> updateSignaturesByYear(@PathVariable String year,
			@RequestBody Map<String, String> body) {
		
		committeescheduleserv.updateCommitteeScheduleSignatureByYear(body, year);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(HttpStatus.OK.toString(),
						"The signature for year " + year
								+ " is updated Successfully"));
	}
	
	@PutMapping("/")
	@Operation(summary = "Update the Committee Schedule", description = "This endpoint updates the Committee Schedule Schedulein the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee Schedule updated Successfully "),
			@ApiResponse(responseCode = "404", description = "Committee Schedule is not updated") })
	public ResponseEntity<ResponseDto> updateCommittee(@RequestBody CommitteeScheduleDto schedulecommittee) {
		committeescheduleserv.updateCommitteeSchedule(schedulecommittee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Committee Schedule is updated successfully"));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete the Committee Schedule", description = "This endpoint delete the Committee Schedule from the database by using its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee Schedule deleted Successfully "),
			@ApiResponse(responseCode = "404", description = "Committee Schedule is not deleted") })
	public ResponseEntity<ResponseDto> updateCommittee(@PathVariable Long id) {
		committeescheduleserv.deleteCommitteeScheduleById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Committee Schedule is Deleted successfully"));
	}
}
