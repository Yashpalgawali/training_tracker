package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.entity.TrainingTimeSlot;
import com.example.demo.service.ITrainingTimeSlotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("trainingtimeslot")
@Tag(name = "TrainingTimeSlot Controller",description = "This controller handles endpoints to perform operations like Save TrainingTimeSlot,Find, Update the trainingtimeslot")
public class TrainingTimeSlotController {

	private final ITrainingTimeSlotService traintimeslotserv;

	/**
	 * @param traintimeslotserv
	 */
	public TrainingTimeSlotController(ITrainingTimeSlotService traintimeslotserv) {
		super(); 
		this.traintimeslotserv = traintimeslotserv;
	}
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/") 
	@Operation(summary = "Save TrainingTimeSlot", description = "This endpoint saves the TrainingTimeSlot object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "201" ,description = "The trainingtimeslot is saved Successfully "),
					@ApiResponse(responseCode = "500" ,description = "The trainingtimeslot is NOT Saved ")
			})
	public ResponseEntity<ResponseDto> saveTrainingTimeSlot(@RequestBody TrainingTimeSlot trainingtimeslot) {
		TrainingTimeSlot savedTrainingTimeSlot = traintimeslotserv.saveTrainingTimeSlot(trainingtimeslot);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseDto(HttpStatus.CREATED.toString(),"TrainingTimeSlot "+savedTrainingTimeSlot.getTraining_time_slot()+" is saved Successfully"));
	}
	
	@GetMapping("/{id}")	 
	@Operation(summary = "Find TrainingTimeSlot By ID", description = "This endpoint finds the TrainingTimeSlot object from the database by its ID" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The trainingtimeslot is found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "The trainingtimeslot is NOT FOUND")
			})
	public ResponseEntity<TrainingTimeSlot> getTrainingTimeSlotById(@PathVariable("id") Long slotid) {
		var trainingtimeslot = traintimeslotserv.getTrainingTimeSlotById(slotid);
		return ResponseEntity.status(HttpStatus.OK).body(trainingtimeslot);
	}
	
	@GetMapping("/")	 
	@Operation(summary = "Find List of TrainingTimeSlots", description = "This endpoint finds the List of TrainingTimeSlots from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The companies are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No companies are found")
			})
	public ResponseEntity<List<TrainingTimeSlot>> getAllTimeSlots() {
		var trainingtimeslot = traintimeslotserv.getAllTimeSlots();
				return ResponseEntity.status(HttpStatus.OK).body(trainingtimeslot);
	}
	
	
	@PutMapping("/")
	@Operation(summary = "Update TrainingTimeSlot", description = "This endpoint UPDATES the TrainingTimeSlot object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The trainingtimeslot is updated Successfully "),
					@ApiResponse(responseCode = "304" ,description = "The trainingtimeslot is NOT updated ")
			})
	public ResponseEntity<ResponseDto> updateTrainingTimeSlot(@RequestBody TrainingTimeSlot trainingtimeslot) {
		var updatedTrainingTimeSlot = traintimeslotserv.saveTrainingTimeSlot(trainingtimeslot);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(new ResponseDto(HttpStatus.OK.toString(),"TrainingTimeSlot "+updatedTrainingTimeSlot.getTraining_time_slot()+" is Updated Successfully"));
	}
}
