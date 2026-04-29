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
import com.example.demo.entity.Frequency;
import com.example.demo.service.IFrequencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("frequency")
@RequiredArgsConstructor
@Tag(name = "Frequency Controller", description = "This controller handles endpoints to perform operations like Find the Frequency")
public class FrequencyController {

	private final IFrequencyService freqserv;

	@GetMapping("/")
	@Operation(summary = "Find List of Frequency", description = "This endpoint finds the List of Frequency from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Frequency are found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Frequency are found") })
	public ResponseEntity<List<Frequency>> getAllFrequencies() {
		var freqlist = freqserv.getAllFrequencies();
		return ResponseEntity.status(HttpStatus.OK).body(freqlist);
	}
	
	@PostMapping("/")
	@Operation(summary = "Save the Frequency", description = "This endpoint saved the Frequency in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Frequency is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Frequency are found") })
	public ResponseEntity<ResponseDto> saveFrequency(@RequestBody Frequency frequency) {
		freqserv.saveFrequency(frequency);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Frequency "+frequency.getFrequency()+" is saved successfully"));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Find the Frequency using ID", description = "This endpoint finds the Frequency from the database by using its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Frequency is found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Frequency is found") })
	public ResponseEntity<Frequency> getFrequencyById(@PathVariable Long id) {
		var frequency = freqserv.getFrequencyById(id);
		return ResponseEntity.status(HttpStatus.OK).body(frequency );
	}
	
	@PutMapping("/")
	@Operation(summary = "Update the Frequency", description = "This endpoint updates the Frequency in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Frequency is updated Successfully "),
			@ApiResponse(responseCode = "404", description = "Frequency is not updated") })
	public ResponseEntity<ResponseDto> updateFrequency(@RequestBody Frequency frequency) {
		freqserv.updateFrequency(frequency);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(), "Frequency "+frequency.getFrequency()+" is updated successfully"));
	}
}
