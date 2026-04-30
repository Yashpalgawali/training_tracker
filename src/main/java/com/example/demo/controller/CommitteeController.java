package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.entity.Committee;
import com.example.demo.service.ICommitteeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("committee")
@RequiredArgsConstructor
@Tag(name = "Committee Controller", description = "This controller handles endpoints to perform operations like Find the Committee")
public class CommitteeController {

	private final ICommitteeService committeeserv;

	@GetMapping("/")
	@Operation(summary = "Find List of Committee", description = "This endpoint finds the List of Committee from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee are found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Committee are found") })
	public ResponseEntity<List<Committee>> getAllFrequencies() {
		var committeeList = committeeserv.getAllCommittees();
		return ResponseEntity.status(HttpStatus.OK).body(committeeList);
	}
	
	@PostMapping("/")
	@Operation(summary = "Save the Committee", description = "This endpoint saved the Committee in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Committee are found") })
	public ResponseEntity<ResponseDto> saveCommittee(@RequestBody Committee committee) {
		committeeserv.saveCommittee(committee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Committee "+committee.getCommitteeName()+" is saved successfully"));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Find the Committee using ID", description = "This endpoint finds the Committee from the database by using its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee is found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Committee is found") })
	public ResponseEntity<Committee> getCommitteeById(@PathVariable Long id) {
		var committee = committeeserv.getCommitteeById(id);
		return ResponseEntity.status(HttpStatus.OK).body(committee );
	}
	
	@PutMapping("/")
	@Operation(summary = "Update the Committee", description = "This endpoint updates the Committee in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Committee is updated Successfully "),
			@ApiResponse(responseCode = "404", description = "Committee is not updated") })
	public ResponseEntity<ResponseDto> updateCommittee(@RequestBody Committee committee) {
		committeeserv.updateCommittee(committee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Committee "+committee.getCommitteeName()+" is updated successfully"));
	}
}
